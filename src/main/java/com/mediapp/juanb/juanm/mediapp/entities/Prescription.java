package com.mediapp.juanb.juanm.mediapp.entities;

import java.sql.Date;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "prescripciones")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_prescription")
    private UUID uuidPrescription;

    @OneToOne
    @JoinColumn(name = "uuid_appointment", referencedColumnName = "uuid_appointment", nullable = false)
    private Appointment appointment;

    @Column(name = "date", nullable = false)
    private Date date;
    
    @Column(name = "instructions", nullable = false)
    private String instructions;

    public Prescription() {
    }

    public Prescription(UUID uuidPrescription, Appointment appointment, Date date, String instructions) {
        this.uuidPrescription = uuidPrescription;
        this.appointment = appointment;
        this.date = date;
        this.instructions = instructions;
    }

    public UUID getUuidPrescription() {
        return uuidPrescription;
    }

    public void setUuidPrescription(UUID uuidPrescription) {
        this.uuidPrescription = uuidPrescription;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
