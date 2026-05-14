package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.dto.interfaces.SaleDetailDtoInter;
import com.example.ventas_bodega.entity.SaleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleDetailRepository extends JpaRepository<SaleDetailEntity, Long> {

    public List<SaleDetailEntity> findBySaleEntity_VentaId(Long id);
    @Query(value = """
        SELECT 
            dv.id_detalle_venta AS id,
            dv.cantidad AS quantity,
            dv.precio_unitario AS unitePrice,
            dv.total AS total,
            dv.id_producto AS productId,
            dv.nombre_producto AS productName,
            dv.notas AS notes,
            dv.unidad_medida AS measurementUnit,
            p.codigo_barras AS barcode
        FROM tb_detalle_venta dv
       INNER JOIN tb_producto p 
            ON p.id_producto = dv.id_producto
        WHERE dv.id_venta = :saleId
        """, nativeQuery = true)
    List<SaleDetailDtoInter> findDetailsBySaleId(@Param("saleId") Long saleId);

}
