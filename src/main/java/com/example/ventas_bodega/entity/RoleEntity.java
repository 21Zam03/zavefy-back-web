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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private CompanyEntity companyEntity;

    @Column(name = "es_sistema_rol")
    private Boolean isSystemRole;

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

}
