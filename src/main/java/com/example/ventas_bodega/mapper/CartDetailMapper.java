package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.CartDetailDto;
import com.example.ventas_bodega.entity.CartDetailEntity;

public class CartDetailMapper {

    public static CartDetailEntity dtoToEntity(CartDetailDto cartDetailDto) {
        CartDetailEntity cartDetailEntity = new CartDetailEntity();
        cartDetailEntity.setQuantity(cartDetailDto.getQuantity());
        cartDetailEntity.setTotal(cartDetailDto.getTotal());
        cartDetailEntity.setPrice(cartDetailDto.getPrice());
        cartDetailEntity.setProductId(cartDetailDto.getProductId());
        cartDetailEntity.setProductName(cartDetailDto.getProductName());
        return cartDetailEntity;
    }



}
