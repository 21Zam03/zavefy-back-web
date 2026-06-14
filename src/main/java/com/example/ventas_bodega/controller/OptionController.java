package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(OptionController.API_PATH)
public class OptionController {


    public static final String API_PATH = "/api/option";

    private OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@RequestParam Long companyId) {
        return new ResponseEntity<>(optionService.getCategories(companyId), HttpStatus.OK);
    }


}
