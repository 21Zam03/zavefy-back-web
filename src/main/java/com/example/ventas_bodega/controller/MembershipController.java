package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MembershipController.API_PATH)
@AllArgsConstructor
public class MembershipController {

    public final static String API_PATH = "/api/membership";

    private final MembershipService membershipService;

    @GetMapping
    ResponseEntity<?> getAllPlans() {
        return new ResponseEntity<>(membershipService.getAllPlans(), HttpStatus.OK);
    }


}
