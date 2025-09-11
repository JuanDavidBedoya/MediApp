package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "citas")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_appointment")
    private UUID uuidAppointment;

    @ManyToOne
    @JoinColumn(name = "cedula_patient", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "cedula_doctor", nullable = false)
    private User doctor;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_date", nullable = false)
    private Date appointmentDate;

    @Temporal(TemporalType.TIME)
    @Column(name = "appointment_time", nullable = false)
    private Date appointmentTime;

    @Column(name = "office", nullable = false)
    private String office;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description")
    private String description;

    public Appointment() {
    }

    public Appointment(UUID uuidAppointment, User patient, User doctor, Date appointmentDate, Date appointmentTime,
            String office, String status, String description) {
        this.uuidAppointment = uuidAppointment;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.office = office;
        this.status = status;
        this.description = description;
    }

    public UUID getUuidAppointment() {
        return uuidAppointment;
    }

    public void setUuidAppointment(UUID uuidAppointment) {
        this.uuidAppointment = uuidAppointment;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
