package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class HistoryStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyStockId;

    private String event;
    private LocalDateTime createDate;
    private Long stockBefore;
    private Long stockAfter;
    private Long stockVariety;
    private Long saleId;
    private Long purchaseId;


}
