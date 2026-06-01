package com.example.ventas_bodega.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class QrUtil {

    private QrUtil() {}

    public static byte[] generateQr(String content, int width, int height)
            throws Exception {

        QRCodeWriter writer = new QRCodeWriter();

        BitMatrix matrix = writer.encode(
                content,
                BarcodeFormat.QR_CODE,
                width,
                height
        );

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(
                matrix,
                "PNG",
                output
        );

        return output.toByteArray();
    }

}
