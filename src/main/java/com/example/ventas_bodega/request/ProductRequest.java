package com.example.ventas_bodega.request;

import com.example.ventas_bodega.enums.MeasurementUnitEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @Pattern(
            regexp = "^\\d{1,20}$",
            message = "El código de barras debe contener máximo 20 dígitos"
    )
    private String barcode;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @NotBlank(message = "La categoria del producto es obligatorio")
    private String category;

    private String imageUrl;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "La unidad de medida es obligatoria")
    private MeasurementUnitEnum measurementUnit;

}
