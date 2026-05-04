package com.example.ventas_bodega.response;

public class ProductResponse {

    private String message;
    private boolean success;

    public ProductResponse() {
    }

    public ProductResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
