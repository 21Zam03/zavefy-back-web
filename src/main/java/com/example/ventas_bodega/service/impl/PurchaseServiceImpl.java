package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.PurchaseDto;
import com.example.ventas_bodega.dto.PurchaseItemDto;
import com.example.ventas_bodega.dto.interfaces.PurchaseDtoInter;
import com.example.ventas_bodega.entity.PurchaseEntity;
import com.example.ventas_bodega.entity.PurchaseItemEntity;
import com.example.ventas_bodega.entity.SupplierEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.PurchaseMapper;
import com.example.ventas_bodega.repository.PurchaseItemRepository;
import com.example.ventas_bodega.repository.PurchaseRepository;
import com.example.ventas_bodega.repository.SupplierRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.InventoryService;
import com.example.ventas_bodega.service.ProductService;
import com.example.ventas_bodega.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PurchaseServiceImpl(
            PurchaseRepository purchaseRepository,
            PurchaseItemRepository purchaseItemRepository,
            SupplierRepository supplierRepository,
            InventoryService inventoryService,
            ProductService productService,
            JdbcTemplate jdbcTemplate) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<PurchaseDto> getPurchasesByCompany(UserEntity user, String searchKey, Long supplierId, String fromDate, String toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurchaseDtoInter> purchases = purchaseRepository.findPurchasesByFilters(
                user.getCompany().getCompanyId(), searchKey, supplierId, fromDate, toDate, pageable
        );
        List<PurchaseDto> data = new ArrayList<>();
        for (int i = 0; i < purchases.getContent().size(); i++) {
            data.add(PurchaseMapper.mapInterfaceToDto(purchases.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, purchases.getTotalElements());
    }

    @Override
    public PurchaseDto getPurchaseById(Long purchaseId, UserEntity user) {
        PurchaseEntity purchaseEntity = purchaseRepository.findByPurchaseIdAndCompanyId(purchaseId, user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("La compra no existe"));

        SupplierEntity supplierEntity = supplierRepository.findBySupplierIdAndCompanyId(purchaseEntity.getSupplierId(), user.getCompany().getCompanyId())
                .orElse(null);
        String supplierName = supplierEntity == null ? null : supplierEntity.getBusinessName();

        List<PurchaseItemEntity> items = purchaseItemRepository.findByPurchaseEntity_PurchaseId(purchaseId);

        return PurchaseMapper.entityToDto(purchaseEntity, supplierName, PurchaseMapper.itemEntityListToDtoList(items));
    }

    @Override
    @Transactional
    public MessageResponse createPurchase(PurchaseDto purchaseDto, UserEntity user) {
        if (purchaseDto == null) {
            throw new IllegalArgumentException("Información de la compra es nula");
        }
        if (purchaseDto.getSupplierId() == null) {
            throw new IllegalArgumentException("La compra no tiene proveedor");
        }
        if (purchaseDto.getItems() == null || purchaseDto.getItems().isEmpty()) {
            throw new IllegalArgumentException("La compra no tiene productos");
        }

        SupplierEntity supplierEntity = supplierRepository.findBySupplierIdAndCompanyId(purchaseDto.getSupplierId(), user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("El proveedor no existe en su inventario"));

        PurchaseEntity purchaseToCreate = PurchaseMapper.dtoToEntity(purchaseDto);
        purchaseToCreate.setCompanyId(user.getCompany().getCompanyId());
        purchaseToCreate.setCreatedBy(Long.valueOf(user.getUserId()));
        PurchaseEntity purchaseCreated = purchaseRepository.save(purchaseToCreate);

        for (PurchaseItemDto itemDto : purchaseDto.getItems()) {
            if (itemDto.getProductId() == null) {
                itemDto.setProductId(createProductForPurchaseItem(itemDto, user));
            } else {
                // El producto ya existía (posiblemente NO_CONTROLADO, creado antes al vuelo desde
                // una venta): esta compra es información real, el dueño sabe cuánta mercadería
                // está entrando, así que deja de estar "no controlado". No toca EN_TRANSICION ni
                // SINCRONIZADO — solo promueve desde NO_CONTROLADO. La lógica de si el stock se
                // fija o se suma vive en InventoryServiceImpl.createHistoryStockForPurchase.
                jdbcTemplate.update(
                        "UPDATE tb_producto SET estado_stock = 'EN_TRANSICION' WHERE id_producto = ? AND estado_stock = 'NO_CONTROLADO'",
                        itemDto.getProductId()
                );
            }
            PurchaseItemEntity itemEntity = PurchaseMapper.itemDtoToEntity(itemDto);
            itemEntity.setPurchaseEntity(purchaseCreated);
            purchaseItemRepository.save(itemEntity);
        }

        MessageResponse stockResponse = inventoryService.createHistoryStockForPurchase(purchaseDto.getItems(), user, purchaseCreated.getPurchaseId());
        if (!stockResponse.isStatus()) {
            // Fuerza el rollback de TODA la compra (cabecera + detalles), no solo del stock
            throw new IllegalStateException(stockResponse.getMessage());
        }

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setPurchaseDto(PurchaseMapper.entityToDto(purchaseCreated, supplierEntity.getBusinessName(), purchaseDto.getItems()));
        messageResponse.setStatus(true);
        messageResponse.setMessage("Compra registrada exitosamente");
        return messageResponse;
    }

    // Crea el producto de un ítem de compra que llegó sin productId (negocio
    // recibiendo mercadería de un producto que aún no existe en su inventario).
    // Reutiliza ProductService.createProduct para heredar todas sus validaciones
    // (duplicado de barcode, generación de código interno, sync con catálogo general).
    private Long createProductForPurchaseItem(PurchaseItemDto itemDto, UserEntity user) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new IllegalArgumentException("Todo producto nuevo en la compra debe tener un nombre");
        }

        ProductDto productDto = new ProductDto();
        productDto.setName(itemDto.getName());
        productDto.setDescription(itemDto.getDescription());
        productDto.setBarcode(itemDto.getBarcode());
        productDto.setPrice(itemDto.getPrice() != null ? itemDto.getPrice() : BigDecimal.ZERO);
        productDto.setCategory(itemDto.getCategory() != null && !itemDto.getCategory().isBlank() ? itemDto.getCategory() : "General");
        productDto.setMeasurementUnit(itemDto.getMeasurementUnit());
        productDto.setImageUrl(itemDto.getImageUrl());
        productDto.setStock(BigDecimal.ZERO);

        try {
            MessageResponse response = productService.createProduct(productDto, user);
            if (!response.isStatus() || response.getProductDto() == null) {
                throw new IllegalStateException(response.getMessage() != null ? response.getMessage() : "No se pudo crear el producto nuevo");
            }
            return response.getProductDto().getProductId();
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo crear el producto \"" + itemDto.getName() + "\": " + e.getMessage(), e);
        }
    }

}
