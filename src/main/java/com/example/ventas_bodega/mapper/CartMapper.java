package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.CartDto;
import com.example.ventas_bodega.entity.CartEntity;
import com.example.ventas_bodega.request.CartRequest;

public class CartMapper {

    public static CartEntity dtoToEntity(CartDto cartDto) {
        CartEntity cartEntity = new CartEntity();
        cartEntity.setCustomerPhoneNumber(cartDto.getCustomerPhoneNumber());
        cartEntity.setCustomerName(cartDto.getCustomerName());
        cartEntity.setSubject(cartDto.getSubject());
        cartEntity.setStatus(cartDto.getStatus());
        cartEntity.setCreatedAt(cartDto.getCreatedAt());
        return cartEntity;
    }

    public static CartDto requestToDto(CartRequest cartRequest) {
        CartDto cartDto = new CartDto();
        cartDto.setCompanyId(cartRequest.getCompanyId());
        cartDto.setCartDetails(cartRequest.getCartDetails());
        cartDto.setCustomerPhoneNumber(cartRequest.getCustomerPhoneNumber());
        cartDto.setCustomerName(cartRequest.getCustomerName());
        cartDto.setSubject(cartRequest.getSubject());
        cartDto.setStatus(cartRequest.getStatus());
        cartDto.setCreatedAt(cartRequest.getCreatedAt());
        return cartDto;
    }

}
