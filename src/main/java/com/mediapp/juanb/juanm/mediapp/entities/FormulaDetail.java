package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "formula_details")
public class FormulaDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_formula_detail", updatable = false, nullable = false, unique = true)
    private UUID idFormulaDetail;

    @ManyToOne
    @JoinColumn(name = "id_formula")
    private Formula formula;

    @ManyToOne
    @JoinColumn(name = "id_medication")
    private Medication medication;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    public FormulaDetail() {}

    public FormulaDetail(Formula formula, Medication medication, int quantity, String dosage) {
        this.formula = formula;
        this.medication = medication;
        this.quantity = quantity;
        this.dosage = dosage;
    }

    public UUID getIdFormulaDetail() {
        return idFormulaDetail;
    }

    public void setIdFormulaDetail(UUID idFormulaDetail) {
        this.idFormulaDetail = idFormulaDetail;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
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

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}