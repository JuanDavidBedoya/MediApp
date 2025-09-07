package com.mediapp.juanb.juanm.mediapp.entities;

import java.time.LocalDate;
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
@Table(name="facturas")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idBill;

    @OneToOne
    @JoinColumn(name = "idCita", referencedColumnName = "idCita", nullable = false)
    private UUID idAppointment;

    @Column(name="fecha")
    private LocalDate date;

    @Column(name="monto")
    private double amount;

    @Column(name="estado")
    private String status;

    public Bill() {
    }

    public Bill(UUID idBill, UUID idAppointment, LocalDate date, double amount, String status) {
        this.idBill = idBill;
        this.idAppointment = idAppointment;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public UUID getIdBill() {
        return idBill;
    }

    public void setIdBill(UUID idBill) {
        this.idBill = idBill;
    }

    public UUID getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(UUID idAppointment) {
        this.idAppointment = idAppointment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
