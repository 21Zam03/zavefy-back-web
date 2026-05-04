package com.example.ventas_bodega.util;

public class ProductUtil {

    public static String generarCodigoEAN13(long numero) {

        String base = "20" + String.format("%010d", numero);

        int suma = 0;
        for (int i = 0; i < base.length(); i++) {
            int digit = Character.getNumericValue(base.charAt(i));
            suma += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (suma % 10)) % 10;

        return base + checkDigit;
    }

}
