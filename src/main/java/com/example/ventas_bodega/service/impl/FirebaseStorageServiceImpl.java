package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.FileDto;
import com.example.ventas_bodega.dto.ProductImageSetDto;
import com.example.ventas_bodega.service.FirebaseStorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    private static final Logger log = LoggerFactory.getLogger(FirebaseStorageServiceImpl.class);

    private static final int THUMB_WIDTH = 150;
    private static final float THUMB_QUALITY = 0.7f;
    private static final int MEDIUM_WIDTH = 500;
    private static final float MEDIUM_QUALITY = 0.75f;
    private static final int LARGE_WIDTH = 1200;
    private static final float LARGE_QUALITY = 0.85f;

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

    @Override
    public ProductImageSetDto uploadProductImages(MultipartFile file, String basePath) throws Exception {
        return uploadProductImages(file.getBytes(), file.getContentType(), basePath);
    }

    @Override
    public ProductImageSetDto uploadProductImagesFromUrl(InputStream inputStream, String contentType, String basePath) throws Exception {
        return uploadProductImages(inputStream.readAllBytes(), contentType, basePath);
    }

    @Override
    public void deleteProductImages(String basePath) {
        deleteFileQuietly(basePath + "/original.jpg");
        deleteFileQuietly(basePath + "/original.png");
        deleteFileQuietly(basePath + "/large.jpg");
        deleteFileQuietly(basePath + "/medium.jpg");
        deleteFileQuietly(basePath + "/thumb.jpg");
    }

    private ProductImageSetDto uploadProductImages(byte[] originalBytes, String contentType, String basePath) throws IOException {
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(originalBytes));

        uploadBytes(originalBytes, contentType, basePath + "/original." + extensionFor(contentType));

        FileDto large = uploadBytes(resize(original, LARGE_WIDTH, LARGE_QUALITY), "image/jpeg", basePath + "/large.jpg");
        FileDto medium = uploadBytes(resize(original, MEDIUM_WIDTH, MEDIUM_QUALITY), "image/jpeg", basePath + "/medium.jpg");
        FileDto thumb = uploadBytes(resize(original, THUMB_WIDTH, THUMB_QUALITY), "image/jpeg", basePath + "/thumb.jpg");

        return new ProductImageSetDto(large, medium, thumb);
    }

    private byte[] resize(BufferedImage original, int maxWidth, float quality) throws IOException {
        int targetWidth = Math.min(maxWidth, original.getWidth());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Thumbnails.of(original)
                .width(targetWidth)
                .outputFormat("jpg")
                .outputQuality(quality)
                .toOutputStream(output);
        return output.toByteArray();
    }

    private FileDto uploadBytes(byte[] bytes, String contentType, String filePath) {
        BlobInfo blobInfo = BlobInfo.newBuilder(StorageClient.getInstance().bucket().getName(), filePath)
                .setContentType(contentType)
                .build();
        Blob blob = StorageClient.getInstance().bucket().create(blobInfo.getName(), bytes, blobInfo.getContentType());

        String encodedFilePath = URLEncoder.encode(filePath, StandardCharsets.UTF_8);
        String url = "https://firebasestorage.googleapis.com/v0/b/" + StorageClient.getInstance().bucket().getName() + "/o/" +
                encodedFilePath + "?alt=media";
        return new FileDto(url, blob.getName());
    }

    private String extensionFor(String contentType) {
        if (contentType != null && contentType.equalsIgnoreCase("image/png")) {
            return "png";
        }
        return "jpg";
    }

    private void deleteFileQuietly(String filePath) {
        try {
            deleteFile(filePath);
        } catch (Exception e) {
            log.warn("No se pudo eliminar el archivo de Firebase: {}", filePath, e);
        }
    }

}
