package com.example.ventas_bodega.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(InventoryController.API_PATH)
public class InventoryController {


    public final static String API_PATH = "/inventory";


}
