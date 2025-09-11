package com.mediapp.juanb.juanm.mediapp.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "histories")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_history")
    private UUID uuidHistory;

    @ManyToOne
    @JoinColumn(name = "cedula", referencedColumnName = "cedula", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "uuid_appointment", referencedColumnName = "uuid_appointment", nullable = false)
    private Appointment appointment;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "description")
    private String description;

    public History() {
    }

    public History(UUID uuidHistory, User patient, Appointment appointment, Date date, String diagnosis,
            String treatment, String description) {
        this.uuidHistory = uuidHistory;
        this.patient = patient;
        this.appointment = appointment;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.description = description;
    }

    public UUID getUuidHistory() {
        return uuidHistory;
    }

    public void setUuidHistory(UUID uuidHistory) {
        this.uuidHistory = uuidHistory;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
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
