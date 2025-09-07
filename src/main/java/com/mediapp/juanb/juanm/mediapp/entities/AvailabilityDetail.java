package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detallesDisponibilidad")
public class AvailabilityDetail {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID uuid;
    @ManyToOne
    @JoinColumn(name = "id_disponibilidad")
    private DoctorAvailability doctorAvailability;
    @Column(name = "dia_semana")
    private String weekDay;
    @Column(name = "hora_inicio")
    private String startTime;
    @Column(name = "hora_fin")
    private String endTime;

    public AvailabilityDetail() {
    }

    public AvailabilityDetail(UUID uuid, DoctorAvailability doctorAvailability, String weekDay, String startTime,
            String endTime) {
        this.uuid = uuid;
        this.doctorAvailability = doctorAvailability;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public DoctorAvailability getDoctorAvailability() {
        return doctorAvailability;
    }

    public void setDoctorAvailability(DoctorAvailability doctorAvailability) {
        this.doctorAvailability = doctorAvailability;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
