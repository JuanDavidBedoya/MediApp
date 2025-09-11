package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "detalles_prescripciones")
public class PrescriptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_prescription_detail")
    private UUID uuidPrescriptionDetail;

    @ManyToOne
    @JoinColumn(name = "uuid_prescription", referencedColumnName = "uuid_prescription", nullable = false)
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "uuid_medication", referencedColumnName = "uuid_medication", nullable = false)
    private Medication medication;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "frequency", nullable = false)
    private String frequency;

    @Column(name = "duration", nullable = false)
    private String duration;

    public PrescriptionDetail() {
    }

    public PrescriptionDetail(UUID uuidPrescriptionDetail, Prescription prescription, Medication medication, int quantity,
                              String frequency, String duration) {
        this.uuidPrescriptionDetail = uuidPrescriptionDetail;
        this.prescription = prescription;
        this.medication = medication;
        this.quantity = quantity;
        this.frequency = frequency;
        this.duration = duration;
    }

    public UUID getUuidPrescriptionDetail() {
        return uuidPrescriptionDetail;
    }

    public void setUuidPrescriptionDetail(UUID uuidPrescriptionDetail) {
        this.uuidPrescriptionDetail = uuidPrescriptionDetail;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
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
