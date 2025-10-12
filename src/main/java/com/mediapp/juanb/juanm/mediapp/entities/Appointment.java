package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_appointment", updatable = false, nullable = false, unique = true)
    private UUID idAppointment;

    @ManyToOne
    @JoinColumn(name = "cedula_doctor")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "cedula_patient")
    private User patient;

    @OneToMany(mappedBy = "appointment")
    private List<Formula> formulas = new ArrayList<>();

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "time", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date time;

    @Column(name = "observations", length = 1000)
    private String observations;

    public Appointment() {}

    public Appointment(UUID idAppointment, Doctor doctor, User patient, List<Formula> formulas, Date date, Date time,
            String observations) {
        this.idAppointment = idAppointment;
        this.doctor = doctor;
        this.patient = patient;
        this.formulas = formulas;
        this.date = date;
        this.time = time;
        this.observations = observations;
    }

    public UUID getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(UUID idAppointment) {
        this.idAppointment = idAppointment;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public List<Formula> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<Formula> formulas) {
        this.formulas = formulas;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}