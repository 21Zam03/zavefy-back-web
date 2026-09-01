package com.example.ventas_bodega.controller;

import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.response.PaymentResponse;
import com.example.ventas_bodega.security.filter.JwtCookieTokenFilter;
import com.example.ventas_bodega.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    // El filtro real requiere JwtUtil/CustomUserDetailService, que @WebMvcTest no carga;
    // se mockea solo para que el contexto de este slice test pueda arrancar (filtros deshabilitados arriba).
    @MockitoBean
    private JwtCookieTokenFilter jwtCookieTokenFilter;

    private String validPayload() {
        return """
            {
              "companyId": "123",
              "source": "YAPE",
              "notificationId": 456,
              "senderName": "Horst Zam*",
              "amount": 1.00,
              "securityCode": "342",
              "receivedAt": "2026-08-31T16:01:07",
              "fingerprint": "456|Horst Zam*|1.00|2026-08-31T16:01:07"
            }
            """;
    }

    @Test
    void receivePayment_conPagoNuevo_debeRetornar201YCreated() throws Exception {
        when(paymentService.receivePayment(any())).thenReturn(new PaymentResponse(true, "CREATED", 1L, "RECEIVED"));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.paymentStatus").value("RECEIVED"));
    }

    @Test
    void receivePayment_conFingerprintDuplicado_debeRetornar200YDuplicate() throws Exception {
        when(paymentService.receivePayment(any())).thenReturn(new PaymentResponse(true, "DUPLICATE", 1L, "RECEIVED"));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DUPLICATE"))
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.paymentStatus").value("RECEIVED"));
    }

    @Test
    void receivePayment_conRequestInvalido_debeRetornar400() throws Exception {
        String invalidPayload = """
            {
              "companyId": "",
              "source": "YAPE",
              "senderName": "Horst Zam*",
              "amount": -1.00,
              "fingerprint": ""
            }
            """;

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void receivePayment_conEmpresaInexistente_debeRetornar404() throws Exception {
        when(paymentService.receivePayment(any())).thenThrow(new NotFoundException("La empresa con id 123 no existe"));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload()))
                .andExpect(status().isNotFound());
    }

}
