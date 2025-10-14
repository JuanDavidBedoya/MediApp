package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

@Table(name = "formulas")
public class Formula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_formula", updatable = false, nullable = false, unique = true)
    private UUID idFormula;

    @ManyToOne
    @JoinColumn(name = "id_appointment")
    private Appointment appointment;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToMany(mappedBy = "formula")
    private List<FormulaDetail> formulaDetails = new ArrayList<>();

    public Formula() {}

    public Formula(Appointment appointment, Date date) {
        this.appointment = appointment;
        this.date = date;
    }

    public UUID getIdFormula() {
        return idFormula;
    }

    public void setIdFormula(UUID idFormula) {
        this.idFormula = idFormula;
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

    public List<FormulaDetail> getFormulaDetails() {
        return formulaDetails;
    }

    public void setFormulaDetails(List<FormulaDetail> formulaDetails) {
        this.formulaDetails = formulaDetails;
    }
}