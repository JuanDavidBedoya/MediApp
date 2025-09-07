package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.repositories.DoctorRepository;

@Service
public class DoctorService {

    private DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> findById(String cedula) {
        return doctorRepository.findById(cedula);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void delete(String cedula) {
        doctorRepository.deleteById(cedula);
    }

    public Doctor update(String cedula, Doctor doctor) {
        doctor.setCedula(cedula);
        return doctorRepository.save(doctor);
    }
}
