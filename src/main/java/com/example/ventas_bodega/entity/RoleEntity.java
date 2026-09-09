package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tb_rol")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer roleId;

    @Column(name = "nombre")
    private String name;

    // "Es un rol predeterminado del sistema" (vs. uno que un negocio cree a futuro) — es
    // taxonomía, NO controla si se puede asignar. Ver `assignable` para eso.
    @Column(name = "es_sistema_rol")
    private Boolean isSystemRole;

    // Controla si el rol aparece como opción en "Invitar usuario" / es aceptado al crear o
    // editar un usuario. Independiente de isSystemRole: SUPER_ADMIN es un rol predeterminado
    // del sistema (isSystemRole=true) pero jamás debe ser asignable por un dueño de negocio.
    @Column(name = "es_asignable")
    private Boolean assignable;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_roles_permisos", joinColumns = @JoinColumn(name = "id_rol"), inverseJoinColumns = @JoinColumn(name = "id_permiso"))
    private Set<PermissionEntity> permissionList;

    public RoleEntity() {

    }

    public RoleEntity(Integer roleId, String name, Set<PermissionEntity> permissionList) {
        this.roleId = roleId;
        this.name = name;
        this.permissionList = permissionList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PermissionEntity> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(Set<PermissionEntity> permissionList) {
        this.permissionList = permissionList;
    }

    public Boolean getIsSystemRole() {
        return isSystemRole;
    }

    public void setIsSystemRole(Boolean isSystemRole) {
        this.isSystemRole = isSystemRole;
    }

    public Boolean getAssignable() {
        return assignable;
    }

    public void setAssignable(Boolean assignable) {
        this.assignable = assignable;
    }

}
