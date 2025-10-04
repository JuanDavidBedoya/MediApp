package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "specialities")
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_speciality", updatable = false, nullable = false, unique = true)
    private UUID idSpeciality;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "speciality")
    private List<Doctor> doctors = new ArrayList<>();

    public Speciality() {}

    public Speciality(String name) {
        this.name = name;
    }

    public UUID getIdSpeciality() {
        return idSpeciality;
    }

    public void setIdSpeciality(UUID idSpeciality) {
        this.idSpeciality = idSpeciality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}