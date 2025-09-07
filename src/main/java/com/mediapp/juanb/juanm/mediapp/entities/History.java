package com.mediapp.juanb.juanm.mediapp.entities;

import java.sql.Date;
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
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idHistory;

    @ManyToOne
    @JoinColumn(name = "cedulaPaciente", referencedColumnName = "cedula", nullable = false)
    private Patient cedulaPatient;
    
    @ManyToOne
    @JoinColumn(name = "idCita", referencedColumnName = "uuid", nullable = false)
    private Appointment idAppointment;

    @Column(name="fecha")
    private Date date;
    @Column(name="diagnostico")
    private String diagnosis;
    @Column(name="tratamiento")
    private String treatment;
    @Column(name="descripcion")
    private String description;

    public History() {
    }

    public History(UUID idHistory, Patient cedulaPatient, Appointment idAppointment, Date date, String diagnosis,
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

    public Patient getCedulaPatient() {
        return cedulaPatient;
    }

    public void setCedulaPatient(Patient cedulaPatient) {
        this.cedulaPatient = cedulaPatient;
    }

    public Appointment getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Appointment idAppointment) {
        this.idAppointment = idAppointment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
