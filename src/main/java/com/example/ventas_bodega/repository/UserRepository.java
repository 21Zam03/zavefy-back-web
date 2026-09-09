package com.example.ventas_bodega.repository;

import com.example.ventas_bodega.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Mantenimiento > Empresas (SUPER_ADMIN): cantidad de usuarios por empresa, para las
    // empresas de la página actual (evita traer todos los usuarios del sistema solo para contar).
    @Query("SELECT u.company.companyId, COUNT(u) FROM UserEntity u WHERE u.company.companyId IN :companyIds GROUP BY u.company.companyId")
    List<Object[]> countUsersByCompanyIds(@Param("companyIds") List<Long> companyIds);

    public Optional<UserEntity> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUserIdAndCompany_CompanyId(Integer userId, Long companyId);
    Optional<UserEntity> findByUserId(Integer userId);

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

    // Usada por Mantenimiento > Usuarios (SUPER_ADMIN): a diferencia de findByCompanyWithFilters,
    // cruza todas las empresas. El texto de búsqueda también matchea nombre/RUC de la empresa,
    // para no necesitar un selector de empresa aparte.
    @Query("""
    SELECT u FROM UserEntity u
    WHERE (
            :searchKey IS NULL
            OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(u.company.comertialName) LIKE LOWER(CONCAT('%', :searchKey, '%'))
            OR LOWER(u.company.ruc) LIKE LOWER(CONCAT('%', :searchKey, '%'))
      )
      AND (:enabled IS NULL OR u.isEnabled = :enabled)
    ORDER BY u.company.comertialName ASC, u.firstname ASC
    """)
    Page<UserEntity> findAllWithFilters(
            @Param("searchKey") String searchKey,
            @Param("enabled") Boolean enabled,
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
