package com.mediapp.juanb.juanm.mediapp.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table (name= "prescripciones")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPrescription;

    @OneToOne
    @JoinColumn(name = "idCita", referencedColumnName = "uuid", nullable = false)
    private Appointment idAppointment;

    @Column(name="fecha")
    private Date date;
    
    @Column(name="instrucciones")
    private String instructions;

    public Prescription() {
    }

    public Prescription(UUID idPrescription, Appointment idAppointment, Date date, String instructions) {
        this.idPrescription = idPrescription;
        this.idAppointment = idAppointment;
        this.date = date;
        this.instructions = instructions;
    }

    public UUID getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(UUID idPrescription) {
        this.idPrescription = idPrescription;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
  
}