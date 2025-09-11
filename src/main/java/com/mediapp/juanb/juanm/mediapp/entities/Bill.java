package com.mediapp.juanb.juanm.mediapp.entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "facturas")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_bill")
    private UUID uuidBill;

    @OneToOne
    @JoinColumn(name = "uuid_appointment", referencedColumnName = "uuid_appointment", nullable = false)
    private Appointment appointment;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "status", nullable = false)
    private String status;

    public Bill() {
    }

    public Bill(UUID uuidBill, Appointment appointment, Date date, double amount, String status) {
        this.uuidBill = uuidBill;
        this.appointment = appointment;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public UUID getUuidBill() {
        return uuidBill;
    }

    public void setUuidBill(UUID uuidBill) {
        this.uuidBill = uuidBill;
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
