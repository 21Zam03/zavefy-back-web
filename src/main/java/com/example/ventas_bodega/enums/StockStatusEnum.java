package com.example.ventas_bodega.enums;

public enum StockStatusEnum {

    // Se vendió/registró sin verificar el stock real (creado al vuelo desde una venta).
    NO_CONTROLADO,
    // Tiene movimientos de stock (compras, creación manual) pero nunca se reconcilió con un conteo físico.
    EN_TRANSICION,
    // El stock quedó fijado por una declaración directa del dueño: un conteo físico (coincida o
    // no con lo que había) o un ajuste manual. En ambos casos el número es igual de confiable,
    // porque viene de la misma fuente: la persona diciendo cuánto hay ahora.
    SINCRONIZADO

}
