package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CartDto;
import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.entity.CartDetailEntity;
import com.example.ventas_bodega.entity.CartEntity;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.mapper.CartDetailMapper;
import com.example.ventas_bodega.mapper.CartMapper;
import com.example.ventas_bodega.mapper.ProductMapper;
import com.example.ventas_bodega.repository.*;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CompanyRepository companyRepository;
    private final OpportunityRepository opportunityRepository;

    @Autowired
    public CatalogServiceImpl(
            ProductRepository productRepository,
            CartRepository cartRepository,
            CartDetailRepository cartDetailRepository,
            CompanyRepository companyRepository,
            OpportunityRepository opportunityRepository
    ) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.companyRepository = companyRepository;
        this.opportunityRepository = opportunityRepository;
    }


    @Override
    public Page<ProductDto> getProductsByCompany(Long id, String barcode, String name, String stockStatus, Boolean active, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productEntityPage = productRepository.findProductsFromCompanyId(id, barcode, name, stockStatus, active, categoryId, pageable);
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity productEntity : productEntityPage.getContent()) {
            ProductDto productDto = ProductMapper.entityToDto(productEntity);
            productDtoList.add(productDto);
        }
        return new PageImpl<>(productDtoList, pageable, productEntityPage.getTotalElements());
    }

    @Override
    public MessageResponse saveCart(CartDto cartDto) {
        MessageResponse messageResponse = new MessageResponse();
        CartEntity cartCreated = new CartEntity();
        try {
            CartEntity cartToCreate = CartMapper.dtoToEntity(cartDto);
            CompanyEntity companyEntity = companyRepository.findById(cartDto.getCompanyId()).get();
            cartToCreate.setCompanyEntity(companyEntity);
            cartToCreate.setCreatedAt(LocalDateTime.now());
            cartToCreate.setStatus("PENDING");
            cartCreated = cartRepository.save(cartToCreate);
            for (int i=0; i<cartDto.getCartDetails().size(); i++) {
                CartDetailEntity cartDetailEntity = CartDetailMapper.dtoToEntity(cartDto.getCartDetails().get(i));
                cartDetailEntity.setCartEntity(cartCreated);
                cartDetailRepository.save(cartDetailEntity);
            }


            messageResponse.setMessage("El carrito de compras se pudo crear exitosamente");
            messageResponse.setStatus(true);
        } catch (Exception e) {
            messageResponse.setStatus(false);
            messageResponse.setMessage("Hubo un error al generar el carrito: "+e.getMessage());
            e.printStackTrace();
        }
        return messageResponse;
    }

}
