package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.dtos.MedicationRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.MedicationResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.MedicationMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public MedicationResponseDTO save(MedicationRequestDTO requestDTO) {

        medicationRepository.findByName(requestDTO.name())
            .ifPresent(med -> {
                throw new IllegalArgumentException("Ya existe un medicamento con el nombre: " + requestDTO.name());
            });

        Medication medication = MedicationMapper.toEntity(requestDTO);
        Medication savedMedication = medicationRepository.save(medication);
        
        return MedicationMapper.toResponseDTO(savedMedication);
    }

    public MedicationResponseDTO update(UUID id, MedicationRequestDTO requestDTO) {
        Medication existingMedication = medicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado con ID: " + id));

        medicationRepository.findByName(requestDTO.name())
            .filter(med -> !med.getIdMedication().equals(id))
            .ifPresent(med -> {
                throw new IllegalArgumentException("Ya existe otro medicamento con el nombre: " + requestDTO.name());
            });

        existingMedication.setName(requestDTO.name());
        existingMedication.setPrice(requestDTO.price());

        Medication updatedMedication = medicationRepository.save(existingMedication);
        return MedicationMapper.toResponseDTO(updatedMedication);
    }

    public MedicationResponseDTO findById(UUID id) {
        Medication medication = medicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado con ID: " + id));
        return MedicationMapper.toResponseDTO(medication);
    }

    public List<MedicationResponseDTO> findAll() {
        return medicationRepository.findAll().stream()
            .map(MedicationMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public void delete(UUID id) {
        Medication medication = medicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado con ID: " + id));

        if (!medication.getFormulaDetails().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar el medicamento porque está asociado a una o más fórmulas.");
        }
        
        medicationRepository.delete(medication);
    }
}