package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.SaleMapper;
import com.example.ventas_bodega.request.SaleRequest;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.FileService;
import com.example.ventas_bodega.service.AgentService;
import com.example.ventas_bodega.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SaleController.API_PATH)
public class SaleController {

    public static final String API_PATH = "/api/sale";

    private final SaleService saleService;
    private final AgentService agentService;
    private final FileService fileService;

    @Autowired
    public  SaleController(SaleService saleService, AgentService agentService, FileService fileService) {
        this.saleService = saleService;
        this.agentService = agentService;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleRequest saleRequest, @CurrentUser UserEntity user) throws Exception {
        MessageResponse messageResponse = saleService.createSale(SaleMapper.requestToDto(saleRequest), user);

        if(user.getCompany().isHasPrinter()) {
            String ticket = fileService.getTicketToPrint(Long.valueOf(messageResponse.getSaleDto().getVentaId()));
            agentService.createPrintJob(user.getCompany().getCompanyId(), user.getCompany().getDefaultAgentId(), ticket);
        }

        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getSaleByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String serial,
            @RequestParam(required = false) Integer number,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @CurrentUser UserEntity user
    ) {
        return new ResponseEntity(this.saleService.getSalesByCompany(user.getCompany().getRuc(), type, serial, number, fromDate, toDate, page, size), HttpStatus.OK);
    }

    @GetMapping("/number")
    public ResponseEntity<?> getNextNumber(@CurrentUser UserEntity user, @RequestParam String type, @RequestParam String serial) {
        return new ResponseEntity<>(saleService.getNextNumber(user, type, serial), HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getDetails(@CurrentUser UserEntity user, @PathVariable Long id) {
        return new ResponseEntity<>(saleService.getDetailsOfSale(user, id), HttpStatus.OK);
    }

}
