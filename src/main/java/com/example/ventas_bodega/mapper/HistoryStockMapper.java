package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.HistoryStockDto;
import com.example.ventas_bodega.dto.interfaces.HistoryStockDtoInter;

import java.util.ArrayList;
import java.util.List;

public class HistoryStockMapper {

    public static HistoryStockDto interfaceToDto(HistoryStockDtoInter historyStockDtoInter) {
        HistoryStockDto historyStockDto = new HistoryStockDto();
        historyStockDto.setHistoryStockId(historyStockDtoInter.getHistoryStockId());
        historyStockDto.setEvent(historyStockDtoInter.getEvent());
        historyStockDto.setCreatedDate(historyStockDtoInter.getCreateDate());
        historyStockDto.setStockBefore(historyStockDtoInter.getStockBefore());
        historyStockDto.setStockAfter(historyStockDtoInter.getStockAfter());
        historyStockDto.setStockVariation(historyStockDtoInter.getStockVariation());
        historyStockDto.setCompanyId(historyStockDtoInter.getCompanyId());
        historyStockDto.setCreatedBy(historyStockDtoInter.getCreatedBy());
        historyStockDto.setCreatedByName(historyStockDtoInter.getCreatedByName());
        historyStockDto.setProductName(historyStockDtoInter.getProductName());
        historyStockDto.setSaleId(historyStockDtoInter.getSaleId());
        historyStockDto.setSaleName(historyStockDtoInter.getSaleName());
        return historyStockDto;
    }

    public static List<HistoryStockDto> interfaceListToDtoList(List<HistoryStockDtoInter> historyStockDtoInterList) {
        List<HistoryStockDto> historyStockDtoList = new ArrayList<>();
        for (HistoryStockDtoInter historyStockDtoInter : historyStockDtoInterList) {
            historyStockDtoList.add(interfaceToDto(historyStockDtoInter));
        }
        return historyStockDtoList;
    }

}
