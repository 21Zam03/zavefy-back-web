package com.example.ventas_bodega.response;

public class PrintJobResponse {

    private Long id;

    private String content;

    private String documentType;

    public PrintJobResponse(Long id, String content, String documentType) {
        this.id = id;
        this.content = content;
        this.documentType = documentType;
    }

    public PrintJobResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

}
