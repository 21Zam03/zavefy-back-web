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
            "STOCK_READ", "STOCK_UPDATE", "BUSINESS_READ", "BUSINESS_USER_READ",
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

    // BUSINESS_USER reemplaza a Vendedor/Almacenero como rol asignable vía "Invitar usuario":
    // une ambos sets de permisos para no perder capacidades al dejar de distinguir entre los dos.
    private static final List<String> BUSINESS_USER_PERMISSIONS = List.of(
            "DASHBOARD_READ", "SALE_CREATE", "SALE_READ", "PRODUCT_READ", "PRODUCT_CREATE",
            "CUSTOMER_READ", "CUSTOMER_CREATE", "SUPPLIER_READ", "SUPPLIER_CREATE",
            "PURCHASE_READ", "PURCHASE_CREATE", "STOCK_READ", "STOCK_UPDATE",
            "FIDE_READ", "FIDE_CREATE", "FIDE_POINTS", "PAY_READ"
    );

    @Override
    public void run(String... args) {
        Map<String, PermissionEntity> permissionsByName = ensurePermissionsExist();
        // isSystemRole=true en TODOS: son predeterminados del sistema (ninguno fue creado por
        // un negocio) — esa es ahora su única función, taxonomía. Lo que decide si aparecen en
        // "Invitar usuario" es `assignable`, no isSystemRole.

        // Administrador/Vendedor/Almacenero: esquema anterior, reemplazado por
        // BUSINESS_ADMIN/BUSINESS_USER. Se dejan de sembrar activamente (no se borran del
        // código por si algún entorno viejo los sigue usando) solo para que empresas/usuarios
        // ya existentes con estos roles sigan funcionando — ensureRoleExists no toca los que ya
        // existen en la base, así que esto no afecta producción de ningún modo. assignable=false:
        // ya no se ofrecen como opción nueva.
        ensureRoleExists("Administrador", ALL_PERMISSIONS, permissionsByName, true, false);
        ensureRoleExists("Vendedor", VENDEDOR_PERMISSIONS, permissionsByName, true, false);
        ensureRoleExists("Almacenero", ALMACENERO_PERMISSIONS, permissionsByName, true, false);
        // SUPER_ADMIN: habilita Mantenimiento (protegido con @PreAuthorize("hasRole('SUPER_ADMIN')")).
        // assignable=false SIEMPRE — un dueño de negocio jamás debe poder auto-otorgarse ni
        // otorgarle a nadie acceso cross-tenant a todo el sistema. No se auto-asigna a nadie:
        // hay que otorgarlo manualmente en la base a la cuenta que deba administrar el sistema.
        ensureRoleExists("SUPER_ADMIN", ALL_PERMISSIONS, permissionsByName, true, false);
        // BUSINESS_ADMIN: rol predeterminado del admin inicial de una empresa nueva (ver
        // MaintenanceServiceImpl.createCompany). assignable=true: un dueño de negocio también
        // puede invitar a otro admin desde "Invitar usuario".
        ensureRoleExists("BUSINESS_ADMIN", ALL_PERMISSIONS, permissionsByName, true, true);
        // BUSINESS_USER: rol para el staff invitado dentro de una empresa (reemplaza a
        // Vendedor/Almacenero). assignable=true: es la opción "Empleado" en "Invitar usuario".
        ensureRoleExists("BUSINESS_USER", BUSINESS_USER_PERMISSIONS, permissionsByName, true, true);
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

    private void ensureRoleExists(String roleName, List<String> permissionNames, Map<String, PermissionEntity> permissionsByName, boolean isSystemRole, boolean assignable) {
        if (roleRepository.findByName(roleName).isPresent()) {
            return;
        }
        RoleEntity role = new RoleEntity();
        role.setName(roleName);
        role.setIsSystemRole(isSystemRole);
        role.setAssignable(assignable);
        Set<PermissionEntity> permissions = permissionNames.stream()
                .map(permissionsByName::get)
                .collect(Collectors.toSet());
        role.setPermissionList(permissions);
        roleRepository.save(role);
    }
}
