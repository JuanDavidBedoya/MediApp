package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Prescription;
import com.mediapp.juanb.juanm.mediapp.repositories.PrescriptionRepository;

@Service
public class PrescriptionService {

    private PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository){
        this.prescriptionRepository = prescriptionRepository;
    }

    public Prescription save(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Prescription update(UUID id, Prescription prescription) {
        prescription.setUuidPrescription(id);
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> findAll() {
        return prescriptionRepository.findAll();
    }

    public Optional <Prescription> findById(UUID id) {
        return prescriptionRepository.findById(id);
    
    }   
    
    public void delete(UUID id) {
        prescriptionRepository.deleteById(id);
    }

}
