package com.mediapp.juanb.juanm.mediapp.mappers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;

@Component
public class FormulaDetailMapper {

    private final FormulaRepository formulaRepository;
    private final MedicationRepository medicationRepository;

    public FormulaDetailMapper(FormulaRepository formulaRepository, MedicationRepository medicationRepository) {
        this.formulaRepository = formulaRepository;
        this.medicationRepository = medicationRepository;
    }

    public FormulaDetail toEntity(FormulaDetailRequestDTO dto) {
        if (dto == null) return null;
        FormulaDetail entity = new FormulaDetail();

        Formula formula = formulaRepository.findById(dto.formulaId())
            .orElseThrow(() -> new ResourceNotFoundException("Fórmula no encontrada con ID: " + dto.formulaId()));
        entity.setFormula(formula);

        Medication medication = medicationRepository.findById(UUID.fromString(dto.medicationId()))
            .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado con ID: " + dto.medicationId()));
        entity.setMedication(medication);

        entity.setQuantity(dto.quantity());
        entity.setDosage(dto.dosage());
        return entity;
    }

    public FormulaDetailResponseDTO toResponseDTO(FormulaDetail entity) {
        if (entity == null) return null;
        return new FormulaDetailResponseDTO(
            entity.getIdFormulaDetail(),
            entity.getFormula().getIdFormula(),
            entity.getFormula().getAppointment().getIdAppointment().toString(),
            entity.getMedication().getName(),
            entity.getQuantity(),
            entity.getDosage()
        );
    }
}