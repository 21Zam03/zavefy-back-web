package com.example.ventas_bodega.response;

import com.example.ventas_bodega.dto.ClientDto;
import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.PurchaseDto;
import com.example.ventas_bodega.dto.ReceivableDto;
import com.example.ventas_bodega.dto.SaleDto;
import com.example.ventas_bodega.dto.SupplierDto;

import java.util.Arrays;

public class MessageResponse {

    private String message;
    private boolean status;
    private ProductDto productDto;
    private SaleDto saleDto;
    private ClientDto clientDto;
    private SupplierDto supplierDto;
    private PurchaseDto purchaseDto;
    private ReceivableDto receivableDto;
    private Object[] object;

    public MessageResponse() {
    }

    public MessageResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
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

    public SupplierDto getSupplierDto() {
        return supplierDto;
    }

    public void setSupplierDto(SupplierDto supplierDto) {
        this.supplierDto = supplierDto;
    }

    public PurchaseDto getPurchaseDto() {
        return purchaseDto;
    }

    public void setPurchaseDto(PurchaseDto purchaseDto) {
        this.purchaseDto = purchaseDto;
    }

    public ReceivableDto getReceivableDto() {
        return receivableDto;
    }

    public void setReceivableDto(ReceivableDto receivableDto) {
        this.receivableDto = receivableDto;
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
