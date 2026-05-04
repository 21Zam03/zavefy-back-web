package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.FileDto;
import com.example.ventas_bodega.service.FirebaseStorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    @Override
    public FileDto uploadFile(MultipartFile file, String filePath) throws Exception {
        InputStream inputStream = file.getInputStream();
        BlobInfo blobInfo = BlobInfo.newBuilder(StorageClient.getInstance().bucket().getName(), filePath)
                .setContentType(file.getContentType())
                .build();
        Blob blob = StorageClient.getInstance().bucket().create(blobInfo.getName(), inputStream, blobInfo.getContentType());
        String encodedFilePath = URLEncoder.encode(filePath, StandardCharsets.UTF_8);
        String url = "https://firebasestorage.googleapis.com/v0/b/" + StorageClient.getInstance().bucket().getName() + "/o/" +
                encodedFilePath + "?alt=media";
        return new FileDto(url, blob.getName());
    }

    @Override
    public FileDto uploadFileFromUrl(InputStream inputStream, String contentType, String filePath) {

        BlobInfo blobInfo = BlobInfo.newBuilder(
                        StorageClient.getInstance().bucket().getName(),
                        filePath
                )
                .setContentType(contentType)
                .build();

        Blob blob = StorageClient.getInstance()
                .bucket()
                .create(blobInfo.getName(), inputStream, blobInfo.getContentType());

        String encodedFilePath = URLEncoder.encode(filePath, StandardCharsets.UTF_8);

        String url = "https://firebasestorage.googleapis.com/v0/b/"
                + StorageClient.getInstance().bucket().getName()
                + "/o/" + encodedFilePath + "?alt=media";

        return new FileDto(url, blob.getName());
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        Blob blob = bucket.get(filePath);

        if(blob != null){
            blob.delete();
            System.out.println("Archivo eliminado correctamente");
        }else{
            System.out.println("Archivo no encontrado");
        }
    }

}
