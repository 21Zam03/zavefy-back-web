package com.example.ventas_bodega.entity;

import com.example.ventas_bodega.enums.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_oportunidad")
public class OpportunityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oportunidad")
    private Long id;

    @Column(name = "codigo")
    private String code; // OP-000001

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private OpportunityStatusEnum status;

    @Column(name = "prioridad")
    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    @Column(name = "numero_contacto")
    private String phoneNumber;

    @Column(name = "nombre_completo")
    private String fullName;

    //private ClientEntity clientEntity;

    @ManyToOne
    @JoinColumn(name = "id_usuario_asignado", nullable = false)
    private UserEntity assignedTo;

    @Column(name = "origen")
    @Enumerated(EnumType.STRING)
    private SourceEnum source;

    @Column(name = "asunto")
    @Enumerated(EnumType.STRING)
    private SubjectEnum subject;

    @Column(name = "mensaje")
    private String message;

    @Column(name = "resultado")
    @Enumerated(EnumType.STRING)
    private ResultEnum result;

    private String lostReason;

    private String internalNotes;

    @OneToMany(mappedBy = "opportunityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpportunityDetailEntity> details = new ArrayList<>();

    @Column(name = "fecha_creacion")
    private LocalDateTime createdDate;

    @Column(name = "fecha_registro")
    private String registerDate;

    @Column(name = "numeracion")
    private Long numeration;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.registerDate = localDateTime.format(formatter);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OpportunityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OpportunityStatusEnum status) {
        this.status = status;
    }

    public PriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(PriorityEnum priority) {
        this.priority = priority;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserEntity getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserEntity assignedTo) {
        this.assignedTo = assignedTo;
    }

    public SourceEnum getSource() {
        return source;
    }

    public void setSource(SourceEnum source) {
        this.source = source;
    }

    public SubjectEnum getSubject() {
        return subject;
    }

    public void setSubject(SubjectEnum subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultEnum getResult() {
        return result;
    }

    public void setResult(ResultEnum result) {
        this.result = result;
    }

    public String getLostReason() {
        return lostReason;
    }

    public void setLostReason(String lostReason) {
        this.lostReason = lostReason;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public List<OpportunityDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(List<OpportunityDetailEntity> details) {
        this.details = details;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public Long getNumeration() {
        return numeration;
    }

    public void setNumeration(Long numeration) {
        this.numeration = numeration;
    }
}
