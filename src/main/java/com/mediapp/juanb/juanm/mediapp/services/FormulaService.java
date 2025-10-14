package com.mediapp.juanb.juanm.mediapp.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaMedicationDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.FormulaMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;

@Service
public class FormulaService {

    private final FormulaRepository formulaRepository;
    private final FormulaMapper formulaMapper;
    private final MedicationRepository medicationRepository;

    public FormulaService(FormulaRepository formulaRepository, FormulaMapper formulaMapper, MedicationRepository medicationRepository) {
        this.formulaRepository = formulaRepository;
        this.formulaMapper = formulaMapper;
        this.medicationRepository = medicationRepository;
    }

     public FormulaResponseDTO save(FormulaRequestDTO requestDTO) {
        // Verificar si ya existe fórmula para esta cita
        formulaRepository.findByAppointmentIdAppointment(requestDTO.appointmentId())
            .ifPresent(formula -> {
                throw new IllegalArgumentException("Esta cita ya tiene una fórmula asociada (ID: " + formula.getIdFormula() + ")");
            });

        // Crear la fórmula base
        Formula formula = formulaMapper.toEntity(requestDTO);
        
        // Agregar los medicamentos a la fórmula
        for (FormulaMedicationDTO medDTO : requestDTO.medications()) {
            Medication medication = medicationRepository.findById(medDTO.medicationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Medicamento no encontrado con ID: " + medDTO.medicationId()));
            
            FormulaDetail detail = new FormulaDetail(formula, medication, medDTO.quantity(), medDTO.dosage());
            formula.getFormulaDetails().add(detail);
        }

        Formula savedFormula = formulaRepository.save(formula);
        return formulaMapper.toResponseDTO(savedFormula);
    }

    public FormulaResponseDTO update(UUID id, FormulaRequestDTO requestDTO) {
        Formula existingFormula = formulaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fórmula no encontrada con ID: " + id));

        if (!existingFormula.getAppointment().getIdAppointment().equals(requestDTO.appointmentId())) {
            throw new IllegalArgumentException("No se permite cambiar la cita a la que una fórmula está asociada.");
        }

        existingFormula.setDate(new Date());
        
        Formula updatedFormula = formulaRepository.save(existingFormula);
        return formulaMapper.toResponseDTO(updatedFormula);
    }

    public FormulaResponseDTO findById(UUID id) {
    // Usar el nuevo método que carga los detalles
    Formula formula = formulaRepository.findByIdWithDetails(id)
        .orElseThrow(() -> new ResourceNotFoundException("Fórmula no encontrada con ID: " + id));
    return formulaMapper.toResponseDTO(formula);
    }

    public List<FormulaResponseDTO> findAll() {
        // Para findAll, podrías necesitar un método similar o usar @EntityGraph
        return formulaRepository.findAll().stream()
            .map(formulaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public void delete(UUID id) {
        Formula formula = formulaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fórmula no encontrada con ID: " + id));

        if (!formula.getFormulaDetails().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar la fórmula porque tiene detalles de medicamentos. Primero elimine los detalles.");
        }

        formulaRepository.delete(formula);
    }
}