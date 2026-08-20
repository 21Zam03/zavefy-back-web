package com.example.ventas_bodega.util;

public class ProductUtil {

    private ProductUtil() {
        // Evita instanciar esta clase de utilidades
    }

    public static String generarCodigoInterno(long numero) {

        if (numero < 0 || numero > 9_999_999_999L) {
            throw new IllegalArgumentException(
                    "El número no puede tener más de 10 dígitos"
            );
        }

        // 2 dígitos de prefijo interno + 10 dígitos del ID
        String base = "20" + String.format("%010d", numero);

        int suma = 0;

        for (int i = 0; i < base.length(); i++) {

            int digit = base.charAt(i) - '0';

            suma += (i % 2 == 0)
                    ? digit
                    : digit * 3;
        }

        int checkDigit = (10 - (suma % 10)) % 10;

        return base + checkDigit;
    }

}
