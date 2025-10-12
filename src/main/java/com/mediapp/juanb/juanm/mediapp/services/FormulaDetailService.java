package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.FormulaDetailMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaDetailRepository;

@Service
public class FormulaDetailService {

    private final FormulaDetailRepository formulaDetailRepository;
    private final FormulaDetailMapper formulaDetailMapper;

    public FormulaDetailService(FormulaDetailRepository formulaDetailRepository, FormulaDetailMapper formulaDetailMapper) {
        this.formulaDetailRepository = formulaDetailRepository;
        this.formulaDetailMapper = formulaDetailMapper;
    }

    public FormulaDetailResponseDTO save(FormulaDetailRequestDTO requestDTO) {

        formulaDetailRepository.findByFormulaIdAndMedicationId(requestDTO.formulaId(), requestDTO.medicationId())
            .ifPresent(detail -> {
                throw new IllegalArgumentException("El medicamento ya está listado en esta fórmula.");
            });

        FormulaDetail formulaDetail = formulaDetailMapper.toEntity(requestDTO);
        FormulaDetail savedDetail = formulaDetailRepository.save(formulaDetail);
        
        return formulaDetailMapper.toResponseDTO(savedDetail);
    }

    public FormulaDetailResponseDTO update(UUID id, FormulaDetailRequestDTO requestDTO) {
        FormulaDetail existingDetail = formulaDetailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de Fórmula no encontrado con ID: " + id));

        if (!existingDetail.getFormula().getIdFormula().equals(requestDTO.formulaId()) || 
            !existingDetail.getMedication().getIdMedication().equals(requestDTO.medicationId())) {
            throw new IllegalArgumentException("No se permite cambiar la fórmula o el medicamento en un detalle de fórmula existente. Debe eliminar y crear uno nuevo.");
        }

        existingDetail.setQuantity(requestDTO.quantity());
        existingDetail.setDosage(requestDTO.dosage());

        FormulaDetail updatedDetail = formulaDetailRepository.save(existingDetail);
        return formulaDetailMapper.toResponseDTO(updatedDetail);
    }

    public FormulaDetailResponseDTO findById(UUID id) {
        FormulaDetail detail = formulaDetailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de Fórmula no encontrado con ID: " + id));
        return formulaDetailMapper.toResponseDTO(detail);
    }

    public List<FormulaDetailResponseDTO> findAll() {
        return formulaDetailRepository.findAll().stream()
            .map(formulaDetailMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public void delete(UUID id) {

        FormulaDetail detail = formulaDetailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de Fórmula no encontrado con ID: " + id));
        
        formulaDetailRepository.delete(detail);
    }
}