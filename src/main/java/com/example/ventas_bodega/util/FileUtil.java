package com.example.ventas_bodega.util;

import java.io.InputStream;
import java.net.URL;

public class FileUtil {

    public static InputStream downloadImage(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        return url.openStream();
    }

}
