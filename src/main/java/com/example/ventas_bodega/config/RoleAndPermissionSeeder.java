package com.example.ventas_bodega.config;

import com.example.ventas_bodega.entity.PermissionEntity;
import com.example.ventas_bodega.entity.RoleEntity;
import com.example.ventas_bodega.repository.PermissionRepository;
import com.example.ventas_bodega.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Siembra el catálogo global de permisos y roles asignables (Administrador, Vendedor,
 * Almacenero) si aún no existen. Los roles y permisos son globales para todas las
 * empresas (no por-empresa), así que esto corre en cada arranque pero es idempotente:
 * solo crea lo que falte por nombre, nunca actualiza roles/permisos ya existentes.
 */
@Component
public class RoleAndPermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public RoleAndPermissionSeeder(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    private static final List<String> ALL_PERMISSIONS = List.of(
            "DASHBOARD_READ", "SALE_CREATE", "SALE_READ", "PRODUCT_READ", "PRODUCT_CREATE",
            "CUSTOMER_READ", "CUSTOMER_CREATE", "SUPPLIER_READ", "SUPPLIER_CREATE",
            "PURCHASE_CREATE", "PURCHASE_READ", "OPPORTUNITY_READ", "PAY_READ",
            "STOCK_READ", "STOCK_UPDATE", "BUSINESS_READ", "USER_READ",
            "FIDE_READ", "FIDE_CREATE", "FIDE_POINTS"
    );

    private static final List<String> VENDEDOR_PERMISSIONS = List.of(
            "DASHBOARD_READ", "SALE_CREATE", "SALE_READ", "PRODUCT_READ",
            "CUSTOMER_READ", "CUSTOMER_CREATE", "STOCK_READ",
            "FIDE_READ", "FIDE_CREATE", "FIDE_POINTS", "PAY_READ"
    );

    private static final List<String> ALMACENERO_PERMISSIONS = List.of(
            "PRODUCT_READ", "PRODUCT_CREATE", "STOCK_READ", "STOCK_UPDATE",
            "SUPPLIER_READ", "SUPPLIER_CREATE", "PURCHASE_READ", "PURCHASE_CREATE"
    );

    @Override
    public void run(String... args) {
        Map<String, PermissionEntity> permissionsByName = ensurePermissionsExist();
        ensureRoleExists("Administrador", ALL_PERMISSIONS, permissionsByName);
        ensureRoleExists("Vendedor", VENDEDOR_PERMISSIONS, permissionsByName);
        ensureRoleExists("Almacenero", ALMACENERO_PERMISSIONS, permissionsByName);
    }

    private Map<String, PermissionEntity> ensurePermissionsExist() {
        Map<String, PermissionEntity> existing = permissionRepository.findAll().stream()
                .collect(Collectors.toMap(PermissionEntity::getName, p -> p));
        for (String name : ALL_PERMISSIONS) {
            if (!existing.containsKey(name)) {
                PermissionEntity toCreate = new PermissionEntity();
                toCreate.setName(name);
                existing.put(name, permissionRepository.save(toCreate));
            }
        }
        return existing;
    }

    private void ensureRoleExists(String roleName, List<String> permissionNames, Map<String, PermissionEntity> permissionsByName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            return;
        }
        RoleEntity role = new RoleEntity();
        role.setName(roleName);
        role.setIsSystemRole(false);
        Set<PermissionEntity> permissions = permissionNames.stream()
                .map(permissionsByName::get)
                .collect(Collectors.toSet());
        role.setPermissionList(permissions);
        roleRepository.save(role);
    }
}
