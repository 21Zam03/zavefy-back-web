package com.example.ventas_bodega.mapper;

import com.example.ventas_bodega.dto.ProductDto;
import com.example.ventas_bodega.dto.ProductFoodDto;
import com.example.ventas_bodega.entity.CategoryEntity;
import com.example.ventas_bodega.entity.ProductEntity;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final BigDecimal IGV = new BigDecimal("0.18");
    private static final BigDecimal FACTOR = BigDecimal.ONE.add(IGV);

    public static ProductDto mapToInternal(ProductFoodDto external) {

        ProductDto product = new ProductDto();

        // Id
        product.setId(null);

        // Barcode
        product.setBarcode(external.getId());

        // Name
        product.setName(external.getProductName());

        // Description (marca + cantidad)
        String description = buildDescription(external.getBrand(), external.getQuantity());
        product.setDescription(description);

        // Image url
        product.setImageUrl(external.getImageUrl());

        // precio
        product.setPrice(BigDecimal.ZERO);

        // Categorias
        product.setCategories(external.getCategoriesList());

        // Activo
        product.setActive(true);

        return product;
    }

    public static ProductEntity dtoToEntity(ProductDto productDto) {
        ProductEntity product = new ProductEntity();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setUnitePrice(calculatePrice(productDto.getPrice()));
        product.setActive(productDto.isActive());
        product.setImageUrl(productDto.getImageUrl());
        //product.setStock(productDto.getStock());
        product.setBarcode(productDto.getBarcode());
        product.setMeasurementUnit(productDto.getMeasurementUnit());
        return product;
    }

    public static ProductDto entityToDto(ProductEntity productEntity) {
        ProductDto product = new ProductDto();
        product.setId(productEntity.getId());
        product.setName(productEntity.getName());
        product.setDescription(productEntity.getDescription());
        product.setPrice(productEntity.getPrice());
        if(productEntity.getUnitPrice() != null) {
            product.setUnitPrice(productEntity.getUnitPrice());
        } else {
            product.setUnitPrice(calculatePrice(productEntity.getPrice()));
        }
        product.setActive(productEntity.isActive());
        product.setImageUrl(productEntity.getImageUrl());
        product.setStock(productEntity.getStock());
        product.setBarcode(productEntity.getBarcode());
        product.setCategories(buildCategoriesInListStringFromEntity(productEntity.getCategoryEntityList()));
        product.setCreatedDate(formatToYearMonthDay(productEntity.getCreatedDate()));
        product.setUpdatedDate(formatToYearMonthDay(productEntity.getUpdatedDate()));
        product.setMeasurementUnit(productEntity.getMeasurementUnit());
        return product;
    }

    public static List<ProductDto> entityListToDtoList(List<ProductEntity> productEntityList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity productEntity : productEntityList) {
            ProductDto productDto = ProductMapper.entityToDto(productEntity);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public static ProductGeneralEntity dtoToEntityGeneral(ProductDto productDto) {
        ProductGeneralEntity product = new ProductGeneralEntity();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategories(buildCategoriesInString(productDto.getCategories()));
        product.setImageUrl(productDto.getImageUrl());
        product.setBarcode(productDto.getBarcode());
        return product;
    }

    public static ProductDto entityGeneralToDto(ProductGeneralEntity productGeneralEntity) {
        ProductDto product = new ProductDto();
        product.setName(productGeneralEntity.getName());
        product.setDescription(productGeneralEntity.getDescription());
        product.setPrice(productGeneralEntity.getPrice());
        product.setActive(true);
        product.setImageUrl(productGeneralEntity.getImageUrl());
        product.setBarcode(productGeneralEntity.getBarcode());
        product.setCategories(buildCategoriesInList(productGeneralEntity.getCategories()));
        return product;
    }

    public static ProductDto buildProductDtoFromController(
            Long id,
            String name,
            String description,
            String price,
            String categories,
            String imageUrl,
            String stock,
            String barcode,
            MultipartFile file,
            String measurementUnit
    ) {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
        productDto.setCategories(categories != null ? Arrays.asList(categories.split(",")) : null);
        productDto.setStock(stock != null ? Integer.parseInt(stock) : 0);
        productDto.setBarcode(barcode);
        productDto.setImageUrl(imageUrl);
        productDto.setFile(file);
        productDto.setMeasurementUnit(measurementUnit);
        return productDto;
    }

    private static String buildCategoriesInString(List<String> categories) {
        return String.join(",", categories);
    }

    private static List<String> buildCategoriesInList(String categories) {
        return Arrays.asList(categories.split(","));
    }

    private static String buildDescription(String brand, String quantity) {

        StringBuilder sb = new StringBuilder();

        if (brand != null && !brand.isBlank()) {
            sb.append("Marca: ").append(brand);
        }

        if (quantity != null && !quantity.isBlank()) {
            if (sb.length() > 0) {
                sb.append(" - ");
            }
            sb.append("Contenido: ").append(quantity);
        }

        return sb.toString();
    }

    private static String buildCategoriesInStringFromEntity(List<CategoryEntity> categories) {
        List<String> categoriesInString = buildCategoriesInListStringFromEntity(categories);
        return String.join(",", categoriesInString);
    }

    private static List<String> buildCategoriesInListStringFromEntity(List<CategoryEntity> categories) {
        List<String> list = new ArrayList<>();
        for (CategoryEntity categoryEntity : categories) {
            list.add(categoryEntity.getName());
        }
        return list;
    }

    public static String formatToYearMonthDay(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }

    public static BigDecimal calculatePrice(BigDecimal price) {
        return price.divide(FACTOR, 2, RoundingMode.HALF_UP);
    }

    public static Object[] entityToObject(ProductEntity productEntity) {
        System.out.println("PRODUCT RECIBIDO: "+productEntity.toString());
        if(productEntity == null) {
            return null;
        } else {
            return new Object[]{
                    productEntity.getId(),
                    productEntity.getName(),
                    productEntity.getDescription(),
                    productEntity.getNotes(),
                    productEntity.getPrice(),
                    productEntity.getBarcode(),
                    productEntity.getCreatedDate(),
                    productEntity.getUpdatedDate()
            };
        }
    }
}
