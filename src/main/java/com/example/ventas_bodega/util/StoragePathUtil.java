package com.example.ventas_bodega.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// Centraliza la carpeta base de Firebase Storage (storage.base-folder en application.yaml)
// para no repetir el literal en cada service que sube un archivo de una empresa.
@Component
public class StoragePathUtil {

    @Value("${storage.base-folder}")
    private String baseFolder;

    public String clientPath(String ruc, String subPath) {
        return baseFolder + "/" + ruc + "/" + subPath;
    }

    // Catálogo compartido (tb_producto_general): no pertenece a ninguna empresa, así que
    // va en "general-products" al mismo nivel que "clients" (no bajo clients/<ruc>).
    // base-folder ya viene como "<root>/clients"; le quitamos ese sufijo para llegar a la raíz.
    public String generalProductPath(String barcode) {
        String root = baseFolder.endsWith("/clients")
                ? baseFolder.substring(0, baseFolder.length() - "/clients".length())
                : baseFolder;
        return root + "/general-products/" + sanitizeBarcode(barcode);
    }

    // El código de barras es texto libre editable desde Mantenimiento; evita que un "/" u
    // otro caracter raro cree subcarpetas inesperadas en Firebase Storage.
    private String sanitizeBarcode(String barcode) {
        return barcode == null ? "sin-codigo" : barcode.replaceAll("[^A-Za-z0-9_-]", "_");
    }

}
