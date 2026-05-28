package com.example.ventas_bodega.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_usuario")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer userId;

    @Column(name = "nombres")
    private String firstname;

    @Column(name = "apellidos")
    private String lastname;

    @Column(name = "correo", unique = true, length = 50, nullable = false)
    private String email;

    @Column(name = "contrasena", length = 100, nullable = true)
    private String password;

    @Column(name= "activo")
    private boolean isEnabled;

    @Column(name= "cuenta_expirada")
    private boolean accountExpired;

    @Column(name= "cuenta_bloqueada")
    private boolean accountLocked;

    @Column(name= "credenciales_expiradas")
    private boolean credentialExpired;

    @Column(name = "nombre_usuario")
    private String username;

    @Column(name = "reseteo_contrasena")
    private boolean passwordReset;

    @Column(name = "fecha_actualizacion_contrasena")
    private LocalDateTime passwordUpdateDate;

    @Column(name = "creado_por")
    private Long createdBy;

    @Column(name = "actualizado_por")
    private Long updatedBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_usuarios_roles", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<RoleEntity> roleList;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private CompanyEntity company;

    public UserEntity() {
    }

    public UserEntity(Integer userId, String email, String password, boolean isEnabled, boolean accountExpired, boolean accountLocked, boolean credentialExpired, Set<RoleEntity> roleList, CompanyEntity company) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialExpired = credentialExpired;
        this.roleList = roleList;
        this.company = company;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(boolean credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    public Set<RoleEntity> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<RoleEntity> roleList) {
        this.roleList = roleList;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
    }

    public LocalDateTime getPasswordUpdateDate() {
        return passwordUpdateDate;
    }

    public void setPasswordUpdateDate(LocalDateTime passwordUpdateDate) {
        this.passwordUpdateDate = passwordUpdateDate;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", accountExpired=" + accountExpired +
                ", accountLocked=" + accountLocked +
                ", credentialExpired=" + credentialExpired +
                ", roleList=" + roleList +
                ", company=" + company +
                '}';
    }

}
