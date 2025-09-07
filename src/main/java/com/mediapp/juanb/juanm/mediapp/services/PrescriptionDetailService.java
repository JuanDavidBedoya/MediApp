package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.PrescriptionDetail;
import com.mediapp.juanb.juanm.mediapp.repositories.PrescriptionDetailRepository;

@Service
public class PrescriptionDetailService {

    private PrescriptionDetailRepository prescriptionDetailRepository;

    public PrescriptionDetailService(PrescriptionDetailRepository prescriptionDetailRepository){
        this.prescriptionDetailRepository = prescriptionDetailRepository;
    }

    public PrescriptionDetail save(PrescriptionDetail prescriptionDetail) {
        return prescriptionDetailRepository.save(prescriptionDetail);
    }

    public PrescriptionDetail update(UUID id, PrescriptionDetail prescriptionDetail) {
        prescriptionDetail.setIdPrescriptionDDetail(id);
        return prescriptionDetailRepository.save(prescriptionDetail);
    }

    public List<PrescriptionDetail> findAll() {
        return prescriptionDetailRepository.findAll();
    }

    public Optional <PrescriptionDetail> findById(UUID id) {
        return prescriptionDetailRepository.findById(id);
    
    }   
    
    public void delete(UUID id) {
        prescriptionDetailRepository.deleteById(id);
    }

}
