package com.example.ventas_bodega.dto;

public class ProductImageSetDto {

    private FileDto large;
    private FileDto medium;
    private FileDto thumb;

    public ProductImageSetDto() {
    }

    public ProductImageSetDto(FileDto large, FileDto medium, FileDto thumb) {
        this.large = large;
        this.medium = medium;
        this.thumb = thumb;
    }

    public FileDto getLarge() {
        return large;
    }

    public void setLarge(FileDto large) {
        this.large = large;
    }

    public FileDto getMedium() {
        return medium;
    }

    public void setMedium(FileDto medium) {
        this.medium = medium;
    }

    public FileDto getThumb() {
        return thumb;
    }

    public void setThumb(FileDto thumb) {
        this.thumb = thumb;
    }
}
