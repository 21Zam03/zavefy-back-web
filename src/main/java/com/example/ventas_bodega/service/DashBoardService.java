package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.DashboardDataDto;
import com.example.ventas_bodega.entity.UserEntity;

public interface DashBoardService {

    DashboardDataDto getDashboardData(String fromDate, String toDate, UserEntity user);

}
