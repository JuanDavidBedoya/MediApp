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

    public MedicationService(MedicationRepository medicationRepository){
        this.medicationRepository = medicationRepository;
    }

    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication update(UUID id, Medication medication) {
        medication.setUuidMedication(id);
        return medicationRepository.save(medication);
    }

    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    public Optional <Medication> findById(UUID id) {
        return medicationRepository.findById(id);
    
    }   
    
    public void delete(UUID id) {
        medicationRepository.deleteById(id);
    }

}
