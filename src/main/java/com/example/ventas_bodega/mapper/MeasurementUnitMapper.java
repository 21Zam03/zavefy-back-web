package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.MeasurementUnitDto;
import com.example.ventas_bodega.entity.MeasurementUnitEntity;

import java.util.ArrayList;
import java.util.List;

public class MeasurementUnitMapper {

    public static MeasurementUnitDto entityToDto(MeasurementUnitEntity measurementUnitEntity) {
        MeasurementUnitDto measurementUnitDto = new MeasurementUnitDto();
        measurementUnitDto.setId(measurementUnitEntity.getId());
        measurementUnitDto.setName(measurementUnitEntity.getName());
        measurementUnitDto.setSymbol(measurementUnitEntity.getSymbol());
        return measurementUnitDto;
    }

    public static List<MeasurementUnitDto> entityListToDtoList(List<MeasurementUnitEntity> measurementUnitEntityList) {
        List<MeasurementUnitDto> measurementUnitDtoList = new ArrayList<>();
        for (MeasurementUnitEntity measurementUnitEntity : measurementUnitEntityList) {
            measurementUnitDtoList.add(entityToDto(measurementUnitEntity));
        }
        return measurementUnitDtoList;
    }

}
