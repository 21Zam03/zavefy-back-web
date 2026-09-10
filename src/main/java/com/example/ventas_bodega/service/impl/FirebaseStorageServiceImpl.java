package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.FileDto;
import com.example.ventas_bodega.dto.ProductImageSetDto;
import com.example.ventas_bodega.service.FirebaseStorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.luciad.imageio.webp.WebPWriteParam;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    private static final Logger log = LoggerFactory.getLogger(FirebaseStorageServiceImpl.class);

    private static final int THUMB_WIDTH = 150;
    private static final float THUMB_QUALITY = 0.7f;
    private static final int MEDIUM_WIDTH = 500;
    private static final float MEDIUM_QUALITY = 0.75f;
    private static final int LARGE_WIDTH = 1200;
    private static final float LARGE_QUALITY = 0.85f;

    // No "immutable"/años: el mismo path se reescribe cuando el producto edita su imagen
    // (basePath fijo por producto en ProductServiceImpl), así que un cache demasiado largo
    // dejaría la imagen vieja pegada en el navegador del cliente tras una edición.
    private static final String CACHE_CONTROL = "public, max-age=86400";

    @Override
    public FileDto uploadFile(MultipartFile file, String filePath) throws Exception {
        InputStream inputStream = file.getInputStream();
        BlobInfo blobInfo = BlobInfo.newBuilder(StorageClient.getInstance().bucket().getName(), filePath)
                .setContentType(file.getContentType())
                .setCacheControl(CACHE_CONTROL)
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
                .setCacheControl(CACHE_CONTROL)
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
        deleteFileQuietly(basePath + "/original.webp");
        // .jpg: productos creados antes de migrar large/medium/thumb a WebP.
        deleteFileQuietly(basePath + "/large.jpg");
        deleteFileQuietly(basePath + "/medium.jpg");
        deleteFileQuietly(basePath + "/thumb.jpg");
        deleteFileQuietly(basePath + "/large.webp");
        deleteFileQuietly(basePath + "/medium.webp");
        deleteFileQuietly(basePath + "/thumb.webp");
    }

    private ProductImageSetDto uploadProductImages(byte[] originalBytes, String contentType, String basePath) throws IOException {
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(originalBytes));

        uploadBytes(originalBytes, contentType, basePath + "/original." + extensionFor(contentType));

        FileDto large = uploadBytes(resize(original, LARGE_WIDTH, LARGE_QUALITY), "image/webp", basePath + "/large.webp");
        FileDto medium = uploadBytes(resize(original, MEDIUM_WIDTH, MEDIUM_QUALITY), "image/webp", basePath + "/medium.webp");
        FileDto thumb = uploadBytes(resize(original, THUMB_WIDTH, THUMB_QUALITY), "image/webp", basePath + "/thumb.webp");

        return new ProductImageSetDto(large, medium, thumb);
    }

    // Redimensiona con Thumbnailator y codifica a WebP con webp-imageio (javax.imageio no
    // trae un encoder de WebP de fábrica). A igual "quality" que antes con JPEG, WebP
    // comprime más chico gracias al códec, así que se reusan los mismos valores de calidad.
    private byte[] resize(BufferedImage original, int maxWidth, float quality) throws IOException {
        int targetWidth = Math.min(maxWidth, original.getWidth());

        BufferedImage resized = Thumbnails.of(original)
                .width(targetWidth)
                .asBufferedImage();

        return encodeWebP(resized, quality);
    }

    private byte[] encodeWebP(BufferedImage image, float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");
        if (!writers.hasNext()) {
            throw new IOException("No hay un ImageWriter de WebP registrado en ImageIO");
        }
        ImageWriter writer = writers.next();
        try {
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            writeParam.setCompressionQuality(quality);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(output)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), writeParam);
            }
            return output.toByteArray();
        } finally {
            writer.dispose();
        }
    }

    private FileDto uploadBytes(byte[] bytes, String contentType, String filePath) {
        BlobInfo blobInfo = BlobInfo.newBuilder(StorageClient.getInstance().bucket().getName(), filePath)
                .setContentType(contentType)
                .setCacheControl(CACHE_CONTROL)
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
        if (contentType != null && contentType.equalsIgnoreCase("image/webp")) {
            return "webp";
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
