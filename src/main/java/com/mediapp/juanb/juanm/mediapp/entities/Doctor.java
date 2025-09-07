package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;

@Entity
public class Doctor extends User {

    @OneToMany
    @JoinTable(name = "doctor_especialidad",
        joinColumns = @JoinColumn(name = "cedula_doctor", referencedColumnName = "cedula"),
        inverseJoinColumns = @JoinColumn(name = "id_especialidad", referencedColumnName = "uuid"))
    private List<Speciality> specialties;

    public Doctor() {
        super();
    }
    
}
