package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_medication", updatable = false, nullable = false, unique = true)
    private UUID idMedication;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @OneToMany(mappedBy = "medication")
    private List<FormulaDetail> formulaDetails = new ArrayList<>();

    public Medication() {}

    public Medication(String name, float price) {
        this.name = name;
        this.price = price;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<FormulaDetail> getFormulaDetails() {
        return formulaDetails;
    }

    public void setFormulaDetails(List<FormulaDetail> formulaDetails) {
        this.formulaDetails = formulaDetails;
    }
}