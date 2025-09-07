package com.mediapp.juanb.juanm.mediapp.entities;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="historiales")
public class Histories {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idHistory;

    @Column(name="cedula")
    private String cedulaPatient;
    
    @ManyToOne
    @JoinColumn(name = "cedula", referencedColumnName = "cedulaPacientes", nullable = false)
    private Long idAppointment;

    @ManyToOne
    @JoinColumn(name = "cedula", referencedColumnName = "cedulaPacientes", nullable = false)

    @Column(name="fecha")
    private LocalDate date;
    @Column(name="diagnostico")
    private String diagnosis;
    @Column(name="tratamiento")
    private String treatment;
    @Column(name="descripcion")
    private String description;

    public Histories() {
    }

    public Histories(UUID idHistory, String cedulaPatient, Long idAppointment, LocalDate date, String diagnosis,
            String treatment, String description) {
        this.idHistory = idHistory;
        this.cedulaPatient = cedulaPatient;
        this.idAppointment = idAppointment;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.description = description;
    }

    public UUID getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(UUID idHistory) {
        this.idHistory = idHistory;
    }

    public String getCedulaPatient() {
        return cedulaPatient;
    }

    public void setCedulaPatient(String cedulaPatient) {
        this.cedulaPatient = cedulaPatient;
    }

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
