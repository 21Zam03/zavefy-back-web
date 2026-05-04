package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.DashboardDataDto;
import com.example.ventas_bodega.dto.ProductAlertDto;
import com.example.ventas_bodega.dto.SalexDay;
import com.example.ventas_bodega.dto.TopProductDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.repository.SaleRepository;
import com.example.ventas_bodega.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DashBoardServiceImpl(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public DashboardDataDto getDashboardData(String fromDate, String toDate, UserEntity user) {
        BigDecimal total = saleRepository.getTotalSalesBetweenDates(fromDate, toDate, user.getCompany().getRuc());

        Long saleCount = saleRepository.countSalesBetweenDates(fromDate, toDate, user.getCompany().getRuc());

        Long productCount = saleRepository.countProductsBetweenDates(fromDate, toDate,  user.getCompany().getRuc());

        BigDecimal averageTicket = saleRepository.getAverageTicketNative(fromDate, toDate, user.getCompany().getRuc());

        List<Object[]> list = saleRepository.getSalesByDayWithZeros(fromDate, toDate, user.getCompany().getRuc());
        List<SalexDay> result = new ArrayList<>();
        for (Object[] row : list) {
            String fecha = row[0].toString();
            BigDecimal totalInDay = (BigDecimal) row[1];
            result.add(new SalexDay(fecha, totalInDay));
        }

        List<Object[]> topProduct = saleRepository.getTopProducts(fromDate, toDate, user.getCompany().getRuc());
        List<TopProductDto> topProductResult = new ArrayList<>();
        for (Object[] row : topProduct) {
            String name = row[0].toString();
            BigDecimal totalSalesBD = (BigDecimal) row[1];
            int totalSales = totalSalesBD.intValue();
            BigDecimal totalIncome = (BigDecimal) row[2];
            topProductResult.add(new TopProductDto(name, totalSales, totalIncome));
        }

        List<Object[]> productAlert = productRepository.getTopLowStockAlerts(user.getCompany().getRuc());
        List<ProductAlertDto> productAlertResult = new ArrayList<>();
        for (Object[] row : productAlert) {
            String name = row[0].toString();
            Long stock = ((Number) row[1]).longValue();
            String stockState = row[2].toString();
            productAlertResult.add(new ProductAlertDto(name, stock, stockState));
        }

        DashboardDataDto dashboardDataDto = new DashboardDataDto();
        dashboardDataDto.setTotal(total);
        dashboardDataDto.setSaleCount(saleCount);
        dashboardDataDto.setProductCount(productCount);
        dashboardDataDto.setAverageTicket(averageTicket);
        dashboardDataDto.setSalexDays(result);
        dashboardDataDto.setTopProducts(topProductResult);
        dashboardDataDto.setProductAlerts(productAlertResult);
        return dashboardDataDto;
    }

}
