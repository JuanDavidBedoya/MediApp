package com.mediapp.juanb.juanm.mediapp.entities;

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
@Table (name="detallePrescripcion")
public class PrescriptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPrescriptionDDetail;

    @ManyToOne
    @JoinColumn(name = "idPrescripcion", referencedColumnName = "idPrescription", nullable = false)
    private Prescription idPrescription;

    @ManyToOne
    @JoinColumn(name = "idMedicamento", referencedColumnName = "idMedication", nullable = false)
    private Medication idMedication;

    @Column(name="cantidad")
    private int quantity;

    @Column(name="frecuencia")
    private String frequency;

    @Column(name="duracion")
    private String duration;

    public PrescriptionDetail() {
    }

    public PrescriptionDetail(UUID idPrescriptionDDetail, Prescription idPrescription, Medication idMedication, int quantity,
            String frequency, String duration) {
        this.idPrescriptionDDetail = idPrescriptionDDetail;
        this.idPrescription = idPrescription;
        this.idMedication = idMedication;
        this.quantity = quantity;
        this.frequency = frequency;
        this.duration = duration;
    }

    public UUID getIdPrescriptionDDetail() {
        return idPrescriptionDDetail;
    }

    public void setIdPrescriptionDDetail(UUID idPrescriptionDDetail) {
        this.idPrescriptionDDetail = idPrescriptionDDetail;
    }

    public Prescription getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(Prescription idPrescription) {
        this.idPrescription = idPrescription;
    }

    public Medication getIdMedication() {
        return idMedication;
    }

    public void setIdMedication(Medication idMedication) {
        this.idMedication = idMedication;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    } 
    
}
