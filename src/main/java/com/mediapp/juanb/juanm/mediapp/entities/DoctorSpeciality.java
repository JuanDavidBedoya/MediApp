package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "especialidadesDoctor")
public class DoctorSpeciality {

    @ManyToOne
    @JoinColumn(name = "cedula_doctor")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "id_especialidad")
    private Speciality speciality;

    public DoctorSpeciality() {
    }

    public DoctorSpeciality(Doctor doctor, Speciality speciality) {
        this.doctor = doctor;
        this.speciality = speciality;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
}
