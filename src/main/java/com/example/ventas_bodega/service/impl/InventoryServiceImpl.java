package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.AdjustmentStockDto;
import com.example.ventas_bodega.dto.HistoryStockDto;
import com.example.ventas_bodega.dto.PurchaseItemDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.dto.interfaces.HistoryStockDtoInter;
import com.example.ventas_bodega.entity.AdjustmentStockEntity;
import com.example.ventas_bodega.entity.HistoryStockEntity;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.SaleDetailEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.enums.StockStatusEnum;
import com.example.ventas_bodega.mapper.AdjustmentStockMapper;
import com.example.ventas_bodega.mapper.HistoryStockMapper;
import com.example.ventas_bodega.repository.AdjustmentStockRepository;
import com.example.ventas_bodega.repository.HistoryStockRepository;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.InventoryService;
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
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final HistoryStockRepository historyStockRepository;
    private final AdjustmentStockRepository adjustmentStockRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InventoryServiceImpl(
            HistoryStockRepository historyStockRepository,
            ProductRepository productRepository,
            AdjustmentStockRepository adjustmentStockRepository,
            JdbcTemplate jdbcTemplate) {
        this.historyStockRepository = historyStockRepository;
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.adjustmentStockRepository = adjustmentStockRepository;
    }

    @Override
    @Transactional
    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity, String event) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            for (SaleDetailDto saleDetailDto : saleDetailDtoList) {
                // Ítems genéricos (ej. "VARIOS") no están ligados a un producto del
                // inventario: no tienen stock que descontar ni historial que registrar.
                if (saleDetailDto.getProductId() == null) {
                    continue;
                }

                ProductEntity product = productRepository.findById(saleDetailDto.getProductId())
                        .orElseThrow(() -> new NotFoundException("El producto con id " + saleDetailDto.getProductId() + " no existe"));

                // Solo NO_CONTROLADO y EN_TRANSICION permiten que el stock quede en negativo:
                // ninguno de los dos viene de un conteo físico confirmado todavía, así que no
                // tiene sentido bloquear la venta por un número que aún no está verificado.
                // SINCRONIZADO sí viene de un conteo/ajuste reciente, así que ahí el stock ya
                // se considera confiable y se aplica el candado estricto.
                boolean allowsNegative = product.getStockStatus() == StockStatusEnum.NO_CONTROLADO
                        || product.getStockStatus() == StockStatusEnum.EN_TRANSICION;
                BigDecimal stockBefore = product.getStock() == null ? BigDecimal.ZERO : product.getStock();
                BigDecimal stockAfter = stockBefore.subtract(saleDetailDto.getQuantity());

                HistoryStockEntity historyStockEntity = new HistoryStockEntity();
                historyStockEntity.setEvent(event);
                historyStockEntity.setStockBefore(stockBefore);
                historyStockEntity.setStockAfter(stockAfter);
                historyStockEntity.setStockVariation(stockAfter.subtract(stockBefore));
                historyStockEntity.setProductId(saleDetailDto.getProductId());
                historyStockEntity.setSaleId(saleDetailDto.getSaleId());
                historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
                historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
                historyStockRepository.save(historyStockEntity);

                int rowsAffected;
                if (allowsNegative) {
                    rowsAffected = jdbcTemplate.update(
                            "UPDATE tb_producto SET stock = stock - ? WHERE id_producto = ?",
                            saleDetailDto.getQuantity(), saleDetailDto.getProductId()
                    );
                } else {
                    rowsAffected = jdbcTemplate.update(
                            "UPDATE tb_producto SET stock = stock - ? WHERE id_producto = ? AND stock >= ?",
                            saleDetailDto.getQuantity(), saleDetailDto.getProductId(), saleDetailDto.getQuantity()
                    );
                    if (rowsAffected == 0) {
                        throw new RuntimeException(
                                "Stock insuficiente o producto no existe. ID: " + saleDetailDto.getProductId()
                        );
                    }
                }
            }

            messageResponse.setMessage("Se creo el historial de forma exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse createHistoryStock(ProductEntity product, UserEntity userEntity, String event) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            HistoryStockEntity historyStockEntity = new HistoryStockEntity();

            BigDecimal stockBefore = BigDecimal.ZERO;
            BigDecimal stockAfter = product.getStock() == null ? BigDecimal.ZERO : product.getStock();
            historyStockEntity.setEvent(event);
            historyStockEntity.setStockBefore(stockBefore);
            historyStockEntity.setStockAfter(stockAfter);
            historyStockEntity.setStockVariation(stockAfter.subtract(stockBefore));
            historyStockEntity.setProductId(product.getId());
            historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
            historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
            historyStockRepository.save(historyStockEntity);
            messageResponse.setMessage("Se creo el historial de forma exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse createHistoryStock(AdjustmentStockEntity adjustmentStockEntity, UserEntity userEntity, String event) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            HistoryStockEntity historyStockEntity = new HistoryStockEntity();

            BigDecimal stockBefore = adjustmentStockEntity.getCurrentStock();
            BigDecimal stockAfter = adjustmentStockEntity.getNewStock();
            historyStockEntity.setEvent(event);
            historyStockEntity.setStockBefore(stockBefore);
            historyStockEntity.setStockAfter(stockAfter);
            historyStockEntity.setStockVariation(stockAfter.subtract(stockBefore));
            historyStockEntity.setProductId(adjustmentStockEntity.getProductId());
            historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
            historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
            historyStockRepository.save(historyStockEntity);

            int rowsAffected = jdbcTemplate.update(
                    "UPDATE tb_producto SET stock = ? WHERE id_producto = ?",
                    historyStockEntity.getStockAfter(),
                    historyStockEntity.getProductId()
            );

            messageResponse.setMessage("Se creo el historial de forma exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    public Page<HistoryStockDto> getHistoryStockByCompany(UserEntity userEntity, String fromDate, String toDate, String event, String searchKey, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoryStockDtoInter> historyStock = historyStockRepository.findHistoryStockByFilters(userEntity.getCompany().getCompanyId(), searchKey, event, fromDate, toDate, pageable);
        List<HistoryStockDto> data = new ArrayList<>();
        for (int i = 0; i<historyStock.getContent().size(); i++) {
            data.add(HistoryStockMapper.interfaceToDto(historyStock.getContent().get(i)));
        }
        return new PageImpl<>(data, pageable, historyStock.getTotalElements());
    }

    @Override
    public MessageResponse createAdjustmentStock(AdjustmentStockDto adjustmentStockDto, UserEntity userEntity) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            AdjustmentStockEntity adjustmentStockEntity = AdjustmentStockMapper.dtoToEntity(adjustmentStockDto);
            adjustmentStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
            AdjustmentStockEntity adjustmentStockCreated = adjustmentStockRepository.save(adjustmentStockEntity);

            createHistoryStock(adjustmentStockCreated, userEntity, "AJUSTE");

            // Un ajuste manual es, por definición, la persona declarando cuál es el stock
            // real correcto en este momento: el producto queda sincronizado.
            jdbcTemplate.update(
                    "UPDATE tb_producto SET estado_stock = ? WHERE id_producto = ?",
                    StockStatusEnum.SINCRONIZADO.name(), adjustmentStockCreated.getProductId()
            );

            messageResponse.setMessage("Se creo el ajuste de stock de manera exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    @Transactional
    public MessageResponse createHistoryStockForPurchase(List<PurchaseItemDto> purchaseItemDtoList, UserEntity userEntity, Long purchaseId) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            for (PurchaseItemDto item : purchaseItemDtoList) {
                ProductEntity product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new NotFoundException("El producto con id " + item.getProductId() + " no existe"));

                BigDecimal stockBefore = product.getStock() == null ? BigDecimal.ZERO : product.getStock();
                // Un stock en 0 o negativo es físicamente imposible: no representa nada real (viene
                // de ventas NO_CONTROLADO/EN_TRANSICION sin respaldo), así que esta compra fija el
                // stock en la cantidad comprada en vez de sumarse sobre un número que nunca fue
                // confiable. Si el stock ya era positivo (aunque el producto no esté SINCRONIZADO
                // todavía), sí es un número físicamente posible y se suma normalmente.
                boolean resetStock = stockBefore.compareTo(BigDecimal.ZERO) <= 0;
                BigDecimal stockAfter = resetStock ? item.getQuantity() : stockBefore.add(item.getQuantity());

                HistoryStockEntity historyStockEntity = new HistoryStockEntity();
                historyStockEntity.setEvent("COMPRA");
                historyStockEntity.setStockBefore(stockBefore);
                historyStockEntity.setStockAfter(stockAfter);
                historyStockEntity.setStockVariation(stockAfter.subtract(stockBefore));
                historyStockEntity.setProductId(item.getProductId());
                historyStockEntity.setPurchaseId(purchaseId);
                historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
                historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
                historyStockRepository.save(historyStockEntity);

                if (resetStock) {
                    jdbcTemplate.update("UPDATE tb_producto SET stock = ? WHERE id_producto = ?", item.getQuantity(), item.getProductId());
                } else {
                    jdbcTemplate.update("UPDATE tb_producto SET stock = stock + ? WHERE id_producto = ?", item.getQuantity(), item.getProductId());
                }
            }

            messageResponse.setMessage("Se actualizo el stock por la compra de forma exitosa");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

    @Override
    @Transactional
    public MessageResponse reverseSaleStock(List<SaleDetailEntity> saleDetailEntityList, UserEntity userEntity, Long saleId) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            for (SaleDetailEntity detail : saleDetailEntityList) {
                // Ítems genéricos (ej. "VARIOS") no tienen id_producto: nada que revertir.
                if (detail.getProductId() == null) {
                    continue;
                }

                ProductEntity product = productRepository.findById(detail.getProductId()).orElse(null);
                // El producto pudo haberse desactivado/eliminado desde la venta original; si ya no
                // existe no hay a qué devolverle el stock, se omite sin interrumpir el resto.
                if (product == null) {
                    continue;
                }

                BigDecimal stockBefore = product.getStock() == null ? BigDecimal.ZERO : product.getStock();
                BigDecimal stockAfter = stockBefore.add(detail.getQuantity());

                HistoryStockEntity historyStockEntity = new HistoryStockEntity();
                historyStockEntity.setEvent("REVERSION_VENTA");
                historyStockEntity.setStockBefore(stockBefore);
                historyStockEntity.setStockAfter(stockAfter);
                historyStockEntity.setStockVariation(stockAfter.subtract(stockBefore));
                historyStockEntity.setProductId(detail.getProductId());
                historyStockEntity.setSaleId(saleId);
                historyStockEntity.setCompanyId(userEntity.getCompany().getCompanyId());
                historyStockEntity.setCreatedBy(Long.valueOf(userEntity.getUserId()));
                historyStockRepository.save(historyStockEntity);

                jdbcTemplate.update(
                        "UPDATE tb_producto SET stock = stock + ? WHERE id_producto = ?",
                        detail.getQuantity(), detail.getProductId()
                );
            }

            messageResponse.setMessage("Se revirtió el stock de la venta original correctamente");
            messageResponse.setStatus(true);
            return messageResponse;
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage(e.getMessage());
            messageResponse.setStatus(false);
            return messageResponse;
        }
    }

}
