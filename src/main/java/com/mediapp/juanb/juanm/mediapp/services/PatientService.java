package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Patient;
import com.mediapp.juanb.juanm.mediapp.repositories.PatientRepository;

@Service
public class PatientService {

    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(String cedula) {
        return patientRepository.findById(cedula);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public void delete(String cedula) {
        patientRepository.deleteById(cedula);
    }

    public Patient update(String cedula, Patient patient) {
        patient.setCedula(cedula);
        return patientRepository.save(patient);
    }
}
