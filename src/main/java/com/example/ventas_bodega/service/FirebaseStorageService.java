package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.FileDto;
import com.example.ventas_bodega.dto.ProductImageSetDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FirebaseStorageService {

    public FileDto uploadFile(MultipartFile file, String keyName) throws Exception;
    public FileDto uploadFileFromUrl(InputStream inputStream, String contentType, String filePath);
    public void deleteFile(String filePath) throws IOException;

    /**
     * Sube la imagen original (sin modificar) junto a 3 variantes comprimidas
     * (large/medium/thumb) bajo la misma carpeta {@code basePath}.
     */
    public ProductImageSetDto uploadProductImages(MultipartFile file, String basePath) throws Exception;
    public ProductImageSetDto uploadProductImagesFromUrl(InputStream inputStream, String contentType, String basePath) throws Exception;
    public void deleteProductImages(String basePath);

}
