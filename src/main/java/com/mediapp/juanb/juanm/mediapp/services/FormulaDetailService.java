package com.mediapp.juanb.juanm.mediapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailsBulkRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaMedicationDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.FormulaDetailMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaDetailRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;

@Service
public class FormulaDetailService {

    private final FormulaDetailRepository formulaDetailRepository;
    private final FormulaDetailMapper formulaDetailMapper;
    private final FormulaRepository formulaRepository;
    private final MedicationRepository medicationRepository;

    public FormulaDetailService(FormulaDetailRepository formulaDetailRepository, FormulaDetailMapper formulaDetailMapper, FormulaRepository formulaRepository, MedicationRepository medicationRepository) {
        this.formulaDetailRepository = formulaDetailRepository;
        this.formulaDetailMapper = formulaDetailMapper;
        this.formulaRepository = formulaRepository;
        this.medicationRepository = medicationRepository;
    }

    public FormulaDetailResponseDTO save(FormulaDetailRequestDTO requestDTO) {
        formulaDetailRepository.findByFormulaIdFormulaAndMedicationName(
            requestDTO.formulaId(), requestDTO.medicationId())
            .ifPresent(detail -> {
                throw new IllegalArgumentException("El medicamento ya está listado en esta fórmula.");
            });

        FormulaDetail formulaDetail = formulaDetailMapper.toEntity(requestDTO);
        FormulaDetail savedDetail = formulaDetailRepository.save(formulaDetail);
        
        return formulaDetailMapper.toResponseDTO(savedDetail);
    }

    public List<FormulaDetailResponseDTO> saveBulk(FormulaDetailsBulkRequestDTO bulkRequest) {
        try {
            System.out.println("🔍 Iniciando saveBulk...");
            System.out.println("🔍 Formula ID: " + bulkRequest.formulaId());
            System.out.println("🔍 Cantidad de medicamentos: " + bulkRequest.medications().size());

            // Verificar que la fórmula existe
            Formula formula = formulaRepository.findById(bulkRequest.formulaId())
                .orElseThrow(() -> {
                    System.out.println("❌ Fórmula no encontrada: " + bulkRequest.formulaId());
                    return new ResourceNotFoundException("Fórmula no encontrada con ID: " + bulkRequest.formulaId());
                });

            System.out.println("✅ Fórmula encontrada: " + formula.getIdFormula());

            List<FormulaDetailResponseDTO> savedDetails = new ArrayList<>();
            List<FormulaDetail> detailsToSave = new ArrayList<>();

            // Validar y preparar cada medicamento
            for (FormulaMedicationDTO medDTO : bulkRequest.medications()) {
                System.out.println("🔍 Procesando medicamento: " + medDTO.name());
                
                // Verificar que el medicamento existe
                System.out.println("🔍 Buscando medicamento por nombre: '" + medDTO.name() + "'");
                Medication medication = medicationRepository.findByName(medDTO.name())
                    .orElseThrow(() -> {
                        System.out.println("❌ Medicamento no encontrado: '" + medDTO.name() + "'");
                        return new ResourceNotFoundException("Medicamento no encontrado: " + medDTO.name());
                    });

                System.out.println("✅ Medicamento encontrado: " + medication.getName());

                // Verificar que no esté duplicado en esta petición
                boolean isDuplicateInRequest = bulkRequest.medications().stream()
                    .filter(m -> m.name().equals(medDTO.name()))
                    .count() > 1;
                
                if (isDuplicateInRequest) {
                    System.out.println("❌ Medicamento duplicado en request: " + medDTO.name());
                    throw new IllegalArgumentException("El medicamento con ID " + medDTO.name() + " está duplicado en la petición.");
                }

                // Verificar que no esté ya en la fórmula
                Optional<FormulaDetail> existingDetail = formulaDetailRepository
                    .findByFormulaIdFormulaAndMedicationName(bulkRequest.formulaId(), medDTO.name());
                
                if (existingDetail.isPresent()) {
                    System.out.println("❌ Medicamento ya existe en fórmula: " + medDTO.name());
                    throw new IllegalArgumentException("El medicamento '" + medication.getName() + "' ya está en la fórmula.");
                }

                // Crear el detalle
                FormulaDetail detail = new FormulaDetail(formula, medication, medDTO.quantity(), medDTO.dosage());
                detailsToSave.add(detail);
                System.out.println("✅ Detalle creado para: " + medication.getName());
            }

            System.out.println("💾 Guardando " + detailsToSave.size() + " detalles...");
            
            // Guardar todos los detalles
            List<FormulaDetail> saved = formulaDetailRepository.saveAll(detailsToSave);
            System.out.println("✅ " + saved.size() + " detalles guardados exitosamente");
            
            // Convertir a DTOs de respuesta
            return saved.stream()
                .map(formulaDetailMapper::toResponseDTO)
                .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("❌ ERROR en saveBulk: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar la excepción
        }
    }

    public FormulaDetailResponseDTO update(UUID id, FormulaDetailRequestDTO requestDTO) {
        FormulaDetail existingDetail = formulaDetailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de Fórmula no encontrado con ID: " + id));

        if (!existingDetail.getFormula().getIdFormula().equals(requestDTO.formulaId()) ||
            !existingDetail.getMedication().getIdMedication().toString().equals(requestDTO.medicationId())) {
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

    public List<FormulaDetailResponseDTO> findByPatientCedula(String cedula) {
        return formulaDetailRepository.findByFormulaAppointmentPatientCedula(cedula).stream()
            .map(formulaDetailMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public void delete(UUID id) {

        FormulaDetail detail = formulaDetailRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Detalle de Fórmula no encontrado con ID: " + id));

        formulaDetailRepository.delete(detail);
    }
}