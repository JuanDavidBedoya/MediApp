package com.mediapp.juanb.juanm.mediapp.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.FormulaMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaRepository;

@Service
public class FormulaService {

    private final FormulaRepository formulaRepository;
    private final FormulaMapper formulaMapper;

    public FormulaService(FormulaRepository formulaRepository, FormulaMapper formulaMapper) {
        this.formulaRepository = formulaRepository;
        this.formulaMapper = formulaMapper;
    }

    public FormulaResponseDTO save(FormulaRequestDTO requestDTO) {
        formulaRepository.findByAppointmentId(requestDTO.appointmentId())
            .ifPresent(formula -> {
                throw new IllegalArgumentException("Esta cita ya tiene una fórmula asociada (ID: " + formula.getIdFormula() + ")");
            });

        Formula formula = formulaMapper.toEntity(requestDTO);
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
        Formula formula = formulaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fórmula no encontrada con ID: " + id));
        return formulaMapper.toResponseDTO(formula);
    }

    public List<FormulaResponseDTO> findAll() {
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