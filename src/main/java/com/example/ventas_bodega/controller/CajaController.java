package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.dto.CajaDto;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.mapper.CajaMapper;
import com.example.ventas_bodega.request.AbrirCajaRequest;
import com.example.ventas_bodega.request.AbrirMovimientoCajaRequest;
import com.example.ventas_bodega.request.CerrarCajaRequest;
import com.example.ventas_bodega.security.annotation.CurrentUser;
import com.example.ventas_bodega.service.CajaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CajaController.API_PATH)
public class CajaController {

    public static final String API_PATH = "/api/caja";

    private final CajaService cajaService;

    @Autowired
    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @GetMapping("/actual")
    public ResponseEntity<?> getSesionActual(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(cajaService.getSesionActual(user.getCompany().getRuc()), HttpStatus.OK);
    }

    @PostMapping("/abrir")
    public ResponseEntity<?> abrirCaja(@Valid @RequestBody AbrirCajaRequest request, @CurrentUser UserEntity user) {
        CajaDto cajaDto = CajaMapper.requestToDto(request);
        return new ResponseEntity<>(cajaService.abrirCaja(cajaDto, user), HttpStatus.CREATED);
    }

    @GetMapping("/movimientos")
    public ResponseEntity<?> getMovimientos(@CurrentUser UserEntity user) {
        return new ResponseEntity<>(cajaService.getMovimientos(user.getCompany().getRuc()), HttpStatus.OK);
    }

    @PostMapping("/movimientos")
    public ResponseEntity<?> registrarMovimiento(@Valid @RequestBody AbrirMovimientoCajaRequest request, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(cajaService.registrarMovimiento(request, user), HttpStatus.CREATED);
    }

    @PostMapping("/cerrar")
    public ResponseEntity<?> cerrarCaja(@Valid @RequestBody CerrarCajaRequest request, @CurrentUser UserEntity user) {
        return new ResponseEntity<>(cajaService.cerrarCaja(request, user), HttpStatus.OK);
    }

    @GetMapping("/historial")
    public ResponseEntity<?> getHistorial(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @CurrentUser UserEntity user
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(cajaService.getHistorial(user.getCompany().getRuc(), pageable), HttpStatus.OK);
    }

}
