package com.example.ventas_bodega.request;

// Fase 1 de "editar venta": solo datos del receptor y notas. No incluye productos,
// montos ni forma de pago a propósito — esos requieren reversión de stock y
// sincronización con caja/cuentas por cobrar, que todavía no existen en el sistema.
public class SaleClientUpdateRequest {

    private String clientName;
    private String clientDocumentType;
    private String clientDocumentNumber;
    private String clientPhoneNumber;
    private String clientAddress;
    private Integer clientId;
    private String notes;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientDocumentType() {
        return clientDocumentType;
    }

    public void setClientDocumentType(String clientDocumentType) {
        this.clientDocumentType = clientDocumentType;
    }

    public String getClientDocumentNumber() {
        return clientDocumentNumber;
    }

    public void setClientDocumentNumber(String clientDocumentNumber) {
        this.clientDocumentNumber = clientDocumentNumber;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
