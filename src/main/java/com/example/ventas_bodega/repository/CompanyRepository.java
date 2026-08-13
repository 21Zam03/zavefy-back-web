package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    public CompanyEntity findByRuc(String ruc);

    @Modifying
    @Transactional
    @Query(
            value = """
            UPDATE tb_empresa
            SET razon_social = :socialReason
            WHERE id_empresa = :companyId
            """,
            nativeQuery = true
    )
    int updateSocialReason(
            @Param("companyId") Long companyId,
            @Param("socialReason") String socialReason
    );

    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE tb_empresa
        SET nombre_comercial = :comertialName,
            direccion = :address,
            correo = :email,
            numero_telefono = :phoneNumber
        WHERE id_empresa = :companyId
        """,
            nativeQuery = true
    )
    int updateBusinessContactInfo(
            @Param("companyId") Long companyId,
            @Param("comertialName") String comertialName,
            @Param("address") String address,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber
    );

    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE tb_empresa
        SET tiene_impresora = :hasPrinter,
            tiene_codigo_barras = :hasBarcode
        WHERE id_empresa = :companyId
        """,
            nativeQuery = true
    )
    int updateBusinessConfiguration(
            @Param("companyId") Long companyId,
            @Param("hasPrinter") boolean hasPrinter,
            @Param("hasBarcode") boolean hasBarcode
    );

    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE tb_empresa
        SET url_imagen = :imageUrl,
            ruta = :filePath
        WHERE id_empresa = :companyId
        """,
            nativeQuery = true
    )
    int updateBusinessBrandInfo(
            @Param("companyId") Long companyId,
            @Param("imageUrl") String imageUrl,
            @Param("filePath") String filePath
    );

}
