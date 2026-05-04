package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.ReniecDataDto;
import com.example.ventas_bodega.service.ReniecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ReniecController.API_PATH)
public class ReniecController {

    public static final String API_PATH = "api/reniec";
    private ReniecService reniecService;

    @Autowired
    public ReniecController(ReniecService reniecService) {
        this.reniecService = reniecService;
    }

    @GetMapping("/{dni}")
    ResponseEntity<ReniecDataDto> getInfoByDni(@PathVariable String dni) throws Exception {
        return new ResponseEntity<>(reniecService.getClienTDataByDni(dni), HttpStatus.OK);
    }

}
