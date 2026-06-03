package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.HistoryStockDto;
import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDetailDto;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    public MessageResponse createHistoryStock(List<SaleDetailDto> saleDetailDtoList, UserEntity userEntity, String event);
    public MessageResponse createHistoryStock(ProductEntity product, UserEntity userEntity, String event);
    public Page<HistoryStockDto> getHistoryStockByCompany(UserEntity userEntity, String fromDate, String toDate, String event, String searchKey, int page, int size);

}
