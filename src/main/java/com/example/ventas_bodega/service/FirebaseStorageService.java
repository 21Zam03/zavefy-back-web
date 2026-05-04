package com.example.ventas_bodega.service;

import com.example.ventas_bodega.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FirebaseStorageService {

    public FileDto uploadFile(MultipartFile file, String keyName) throws Exception;
    public FileDto uploadFileFromUrl(InputStream inputStream, String contentType, String filePath);
    public void deleteFile(String filePath) throws IOException;

}
