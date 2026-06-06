package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.AdjustmentStockDto;
import com.example.ventas_bodega.entity.AdjustmentStockEntity;
import com.example.ventas_bodega.request.AdjustmentStockRequest;

public class AdjustmentStockMapper {

    public static AdjustmentStockEntity dtoToEntity(AdjustmentStockDto adjustmentStockDto) {
        AdjustmentStockEntity adjustmentStockEntity = new AdjustmentStockEntity();
        adjustmentStockEntity.setProductId(adjustmentStockDto.getProductId());
        adjustmentStockEntity.setCurrentStock(adjustmentStockDto.getCurrentStock());
        adjustmentStockEntity.setAdjustmentType(adjustmentStockDto.getAdjustmentType());
        adjustmentStockEntity.setQuantity(adjustmentStockDto.getQuantity());
        adjustmentStockEntity.setNewStock(adjustmentStockDto.getNewStock());
        adjustmentStockEntity.setReason(adjustmentStockDto.getReason());
        adjustmentStockEntity.setObservation(adjustmentStockDto.getObservation());
        //adjustmentStockEntity.setCreatedBy(adjustmentStockDto.getCreatedBy());
        //adjustmentStockEntity.setCreatedDate(adjustmentStockDto.getCreatedDate());
        return adjustmentStockEntity;
    }

    public static AdjustmentStockDto requestToDto(AdjustmentStockRequest adjustmentStockRequest) {
        AdjustmentStockDto adjustmentStockDto = new AdjustmentStockDto();
        adjustmentStockDto.setProductId(adjustmentStockRequest.getProductId());
        adjustmentStockDto.setCurrentStock(adjustmentStockRequest.getCurrentStock());
        adjustmentStockDto.setAdjustmentType(adjustmentStockDto.getAdjustmentType());
        adjustmentStockDto.setQuantity(adjustmentStockRequest.getQuantity());
        adjustmentStockDto.setNewStock(adjustmentStockRequest.getNewStock());
        adjustmentStockDto.setReason(adjustmentStockRequest.getReason());
        adjustmentStockDto.setObservation(adjustmentStockRequest.getObservation());
        return adjustmentStockDto;
    }

}
