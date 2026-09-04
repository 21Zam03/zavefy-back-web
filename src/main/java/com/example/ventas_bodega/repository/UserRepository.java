package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUserIdAndCompany_CompanyId(Integer userId, Long companyId);

    @Query("""
    SELECT u FROM UserEntity u
    WHERE u.company.companyId = :companyId
      AND (
            :searchKey IS NULL
            OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchKey, '%'))
      )
    ORDER BY u.firstname ASC
    """)
    Page<UserEntity> findByCompanyWithFilters(
            @Param("companyId") Long companyId,
            @Param("searchKey") String searchKey,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(
            value = """
            UPDATE tb_usuario
            SET nombres = :firstname,
                apellidos = :lastname,
                correo = :email,
                actualizado_por = :updatedBy
            WHERE id_usuario = :userId
            """,
            nativeQuery = true
    )
    int updateAccountInfo(
            @Param("userId") Integer userId,
            @Param("firstname") String firstname,
            @Param("lastname") String lastname,
            @Param("email") String email,
            @Param("updatedBy") Long updatedBy
    );
    Optional<UserEntity> findByEmail(String email);


    @Modifying
    @Transactional
    @Query(
            value = """
        UPDATE tb_usuario
        SET contrasena = :password,
            fecha_actualizacion_contrasena = NOW(),
            reseteo_contrasena = false,
            actualizado_por = :updatedBy
        WHERE id_usuario = :userId
        """,
            nativeQuery = true
    )
    int updatePassword(
            @Param("userId") Integer userId,
            @Param("password") String password,
            @Param("updatedBy") Long updatedBy
    );

}
