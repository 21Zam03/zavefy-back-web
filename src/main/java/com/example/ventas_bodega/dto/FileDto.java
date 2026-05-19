package com.example.ventas_bodega.dto;

public class FileDto {

    private String url;
    private String fileName;

    public FileDto() {

    }

    public FileDto(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FileDto{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
