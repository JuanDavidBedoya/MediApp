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
@Table (name="DetallePrescripcion")
public class PrescriptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idPrescriptionDDetail;

    @ManyToOne
    @JoinColumn(name = "idPrescripcion", referencedColumnName = "idPrescripcion", nullable = false)
    private UUID idPrescription;

    @ManyToOne
    @JoinColumn(name = "idMedicamento", referencedColumnName = "idMedicamento", nullable = false)
    private UUID idMedication;

    @Column(name="cantidad")
    private int quantity;

    @Column(name="frecuencia")
    private String frequency;

    @Column(name="duracion")
    private String duration;

    public PrescriptionDetail() {
    }

    public PrescriptionDetail(UUID idPrescriptionDDetail, UUID idPrescription, UUID idMedication, int quantity,
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

    public UUID getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(UUID idPrescription) {
        this.idPrescription = idPrescription;
    }

    public UUID getIdMedication() {
        return idMedication;
    }

    public void setIdMedication(UUID idMedication) {
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
