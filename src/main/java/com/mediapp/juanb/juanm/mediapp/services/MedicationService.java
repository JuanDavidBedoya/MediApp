package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;

@Service
public class MedicationService {

    private MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    public Optional<Medication> findById(UUID uuid) {
        return medicationRepository.findById(uuid);
    }

    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }

    public void delete(UUID uuid) {
        medicationRepository.deleteById(uuid);
    }

    public Medication update(UUID uuid, Medication medication) {
        medication.setIdMedication(uuid);
        return medicationRepository.save(medication);
    }
}