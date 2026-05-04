package com.example.ventas_bodega.repository;
;
import com.example.ventas_bodega.entity.ProductGeneralEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductGeneralRepository extends JpaRepository<ProductGeneralEntity, Long> {

    boolean existsByBarcode(String barcode);
    ProductGeneralEntity findByBarcode(String barcode);

}
