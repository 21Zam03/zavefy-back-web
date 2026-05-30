package com.example.ventas_bodega.response;

import com.example.ventas_bodega.dto.ClientDto;
import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.SaleDto;

import java.util.Arrays;

public class MessageResponse {

    private String message;
    private boolean status;
    private ProductDto productDto;
    private SaleDto saleDto;
    private ClientDto clientDto;
    private Object[] object;

    public MessageResponse() {
    }

    public MessageResponse(String message, boolean status, ProductDto productDto) {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public boolean isStatus() {
        return status;
    }

    public SaleDto getSaleDto() {
        return saleDto;
    }

    public void setSaleDto(SaleDto saleDto) {
        this.saleDto = saleDto;
    }

    public Object[] getObject() {
        return object;
    }
    public void setObject(Object[] object) {
        this.object = object;
    }

    public ClientDto getClientDto() {
        return clientDto;
    }

    public void setClientDto(ClientDto clientDto) {
        this.clientDto = clientDto;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", productDto=" + productDto +
                ", saleDto=" + saleDto +
                ", object=" + Arrays.toString(object) +
                '}';
    }
}
