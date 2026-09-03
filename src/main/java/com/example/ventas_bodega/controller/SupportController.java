package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.request.CreateSupportMessageRequest;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SupportController.API_PATH)
public class SupportController {

    public static final String API_PATH = "/api/support";

    private final SupportService supportService;

    @Autowired
    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody CreateSupportMessageRequest request, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(supportService.createMessage(request, user), HttpStatus.CREATED);
    }

}
