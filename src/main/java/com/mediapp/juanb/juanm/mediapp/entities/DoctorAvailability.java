package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "disponibilidad_doctor")
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_doctor_availability")
    private UUID uuidDoctorAvailability;

    @ManyToOne
    @JoinColumn(name = "cedula_doctor", referencedColumnName = "cedula")
    private User doctor;

    @Column(name = "description", nullable = false)
    private String description;

    public DoctorAvailability() {
    }

    public DoctorAvailability(UUID uuidDoctorAvailability, User doctor, String description) {
        this.uuidDoctorAvailability = uuidDoctorAvailability;
        this.doctor = doctor;
        this.description = description;
    }

    public UUID getUuid() {
        return uuidDoctorAvailability;
    }

    public void setUuid(UUID uuidDoctorAvailability) {
        this.uuidDoctorAvailability = uuidDoctorAvailability;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
