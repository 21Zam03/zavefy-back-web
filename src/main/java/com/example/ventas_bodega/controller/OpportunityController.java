package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.OpportunityService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(OpportunityController.API_PATH)
public class OpportunityController {

    public final static String API_PATH = "/api/opportunity";
    private final OpportunityService opportunityService;

    @Autowired
    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @GetMapping
    public ResponseEntity<?> getOpportunitiesFromBusiness(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) throws BadRequestException {
        return new ResponseEntity(this.opportunityService.getOpportunitiesByCompany(user, status, priority, fromDate, toDate, source, subject, result, page, size), HttpStatus.OK);
    }


}
