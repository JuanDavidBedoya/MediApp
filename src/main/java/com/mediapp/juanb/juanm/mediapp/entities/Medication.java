package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "medicamentos")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_medication")
    private UUID uuidMedication;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "presentation")
    private String presentation;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public Medication() {
    }

    public Medication(UUID uuidMedication, String name, String description, String dosage, String presentation, Integer quantity) {
        this.uuidMedication = uuidMedication;
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.presentation = presentation;
        this.quantity = quantity;
    }

    public UUID getUuidMedication() {
        return uuidMedication;
    }

    public void setUuidMedication(UUID uuidMedication) {
        this.uuidMedication = uuidMedication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
