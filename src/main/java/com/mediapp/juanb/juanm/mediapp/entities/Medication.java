package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name="Medicamentos")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idMedication;

    @Column(name="cantidad")
    private String name;

    @Column(name="descripcion")
    private String description;

    @Column(name="dosis")
    private String dosage;

    @Column(name="presentacion")
    private String presentation;

    public Medication() {
    }

    public Medication(UUID idMedication, String name, String description, String dosage, String presentation) {
        this.idMedication = idMedication;
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.presentation = presentation;
    }

    public UUID getIdMedication() {
        return idMedication;
    }

    public void setIdMedication(UUID idMedication) {
        this.idMedication = idMedication;
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

}

