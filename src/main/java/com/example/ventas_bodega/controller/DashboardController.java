package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.DashboardDataDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DashboardController.API_PATH)
public class DashboardController {

    public static final String API_PATH = "api/dashboard";
    private final DashBoardService dashBoardService;

    @Autowired
    public DashboardController(DashBoardService dashBoardService) {
        this.dashBoardService = dashBoardService;
    }

    @GetMapping
    public ResponseEntity<DashboardDataDto> getDashboardData(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user) {
        return new ResponseEntity<>(dashBoardService.getDashboardData(fromDate, toDate, user), HttpStatus.OK);
    }

}
