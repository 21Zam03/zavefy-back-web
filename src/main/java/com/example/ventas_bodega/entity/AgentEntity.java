package com.example.ventas_bodega.entity;

import com.example.ventas_bodega.enums.AgentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_agente")
public class AgentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_empresa", nullable = false)
    private Long companyId;

    @Column(name = "uuid_maquina", nullable = false, unique = true, length = 100)
    private String machineUuid;

    @Column(name = "nombre_maquina", nullable = false, length = 100)
    private String machineName;

    @Column(name = "impresora_predeterminada", length = 150)
    private String defaultPrinter;

    @Column(name = "version", length = 20)
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AgentStatus status;

    @Column(name = "ultimo_visto")
    private LocalDateTime lastSeen;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();

        if (this.status == null) {
            this.status = AgentStatus.ONLINE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getDefaultPrinter() {
        return defaultPrinter;
    }

    public void setDefaultPrinter(String defaultPrinter) {
        this.defaultPrinter = defaultPrinter;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
