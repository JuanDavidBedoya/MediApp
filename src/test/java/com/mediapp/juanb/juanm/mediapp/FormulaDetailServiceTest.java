package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.mediapp.juanb.juanm.mediapp.services.FormulaDetailService;

@ExtendWith(MockitoExtension.class)
class FormulaDetailServiceTest {

    @Mock
    private FormulaDetailRepository formulaDetailRepository;

    @Mock
    private FormulaDetailMapper formulaDetailMapper;

    @InjectMocks
    private FormulaDetailService formulaDetailService;

     @Mock
    private FormulaRepository formulaRepository;

    @Mock
    private MedicationRepository medicationRepository;

    private List<FormulaDetailRequestDTO> requestList;
    private List<FormulaDetail> mockDetails;
    private List<UUID> formulaIds;
    private List<UUID> medicationIds;

    @BeforeEach
    void setUp() {
        requestList = new ArrayList<>();
        mockDetails = new ArrayList<>();
        formulaIds = new ArrayList<>();
        medicationIds = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            UUID formulaId = UUID.randomUUID();
            UUID medicationId = UUID.randomUUID();
            UUID detailId = UUID.randomUUID();

            formulaIds.add(formulaId);
            medicationIds.add(medicationId);

            FormulaDetailRequestDTO req = new FormulaDetailRequestDTO(
                    formulaId,
                    medicationId,
                    i * 2, 
                    "Cada " + (6 + i * 2) + " horas"
            );
            requestList.add(req);

            FormulaDetail detail = new FormulaDetail();
            detail.setIdFormulaDetail(detailId);

            Formula formula = new Formula();
            formula.setIdFormula(formulaId);

            Medication medication = new Medication();
            medication.setIdMedication(medicationId);

            detail.setFormula(formula);
            detail.setMedication(medication);
            detail.setQuantity(i * 2);
            detail.setDosage("Cada " + (6 + i * 2) + " horas"); 

            mockDetails.add(detail);
        }
    }

    @Test
    void save_Success() {
        FormulaDetailRequestDTO request = requestList.get(0);
        FormulaDetail mockDetail = mockDetails.get(0);
        UUID formulaId = formulaIds.get(0);
        UUID medicationId = medicationIds.get(0);

        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, medicationId))
                .thenReturn(Optional.empty());
        when(formulaDetailMapper.toEntity(request)).thenReturn(mockDetail);
        when(formulaDetailRepository.save(any(FormulaDetail.class))).thenReturn(mockDetail);
        when(formulaDetailMapper.toResponseDTO(any(FormulaDetail.class)))
                .thenReturn(new FormulaDetailResponseDTO(
                        mockDetail.getIdFormulaDetail(),
                        formulaId,
                        medicationId,
                        2,
                        "Cada 8 horas"
                ));

        FormulaDetailResponseDTO result = formulaDetailService.save(request);

        assertNotNull(result);
        verify(formulaDetailRepository, times(1)).save(any(FormulaDetail.class));
    }

    @Test
    void save_Fails_MedicationAlreadyInFormula() {
        FormulaDetailRequestDTO request = requestList.get(1);
        FormulaDetail mockDetail = mockDetails.get(1);
        UUID formulaId = formulaIds.get(1);
        UUID medicationId = medicationIds.get(1);

        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, medicationId))
                .thenReturn(Optional.of(mockDetail));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.save(request);
        }, "Debe lanzar excepción si el medicamento ya está en la fórmula.");

        verify(formulaDetailRepository, never()).save(any(FormulaDetail.class));
    }

    @Test
    void update_Fails_ChangingImmutableFields() {
        FormulaDetail mockDetail = mockDetails.get(2);
        UUID detailId = mockDetail.getIdFormulaDetail();
        UUID formulaId = formulaIds.get(2);
        UUID medicationId = medicationIds.get(2);

        FormulaDetailRequestDTO invalidRequest = new FormulaDetailRequestDTO(
                formulaId,
                UUID.randomUUID(),
                10,
                "Dos veces al día"
        );

        when(formulaDetailRepository.findById(detailId)).thenReturn(Optional.of(mockDetail));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.update(detailId, invalidRequest);
        }, "No debe permitir cambiar la fórmula o el medicamento.");

        verify(formulaDetailRepository, never()).save(any(FormulaDetail.class));
    }

    @Test
    void saveBulk_Success() {

        UUID formulaId = formulaIds.get(0);
        UUID medicationId1 = medicationIds.get(0);
        UUID medicationId2 = medicationIds.get(1);
        UUID medicationId3 = medicationIds.get(2);

        List<FormulaMedicationDTO> medications = Arrays.asList(
            new FormulaMedicationDTO(medicationId1, 2, "Cada 8 horas"),
            new FormulaMedicationDTO(medicationId2, 1, "Cada 12 horas"),
            new FormulaMedicationDTO(medicationId3, 3, "Cada 6 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);
        
        Medication medication1 = new Medication("Ibuprofeno", 15000);
        medication1.setIdMedication(medicationId1);
        
        Medication medication2 = new Medication("Amoxicilina", 25000);
        medication2.setIdMedication(medicationId2);
        
        Medication medication3 = new Medication("Paracetamol", 8000);
        medication3.setIdMedication(medicationId3);

        FormulaDetail detail1 = new FormulaDetail(mockFormula, medication1, 2, "Cada 8 horas");
        detail1.setIdFormulaDetail(UUID.randomUUID());
        
        FormulaDetail detail2 = new FormulaDetail(mockFormula, medication2, 1, "Cada 12 horas");
        detail2.setIdFormulaDetail(UUID.randomUUID());
        
        FormulaDetail detail3 = new FormulaDetail(mockFormula, medication3, 3, "Cada 6 horas");
        detail3.setIdFormulaDetail(UUID.randomUUID());

        List<FormulaDetail> savedDetails = Arrays.asList(detail1, detail2, detail3);

        FormulaDetailResponseDTO response1 = new FormulaDetailResponseDTO(
            detail1.getIdFormulaDetail(), formulaId, medicationId1, 2, "Cada 8 horas");
        FormulaDetailResponseDTO response2 = new FormulaDetailResponseDTO(
            detail2.getIdFormulaDetail(), formulaId, medicationId2, 1, "Cada 12 horas");
        FormulaDetailResponseDTO response3 = new FormulaDetailResponseDTO(
            detail3.getIdFormulaDetail(), formulaId, medicationId3, 3, "Cada 6 horas");

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        
        when(medicationRepository.findById(medicationId1)).thenReturn(Optional.of(medication1));
        when(medicationRepository.findById(medicationId2)).thenReturn(Optional.of(medication2));
        when(medicationRepository.findById(medicationId3)).thenReturn(Optional.of(medication3));
        
        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, medicationId1))
            .thenReturn(Optional.empty());
        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, medicationId2))
            .thenReturn(Optional.empty());
        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, medicationId3))
            .thenReturn(Optional.empty());
        
        when(formulaDetailRepository.saveAll(anyList())).thenReturn(savedDetails);
        
        when(formulaDetailMapper.toResponseDTO(detail1)).thenReturn(response1);
        when(formulaDetailMapper.toResponseDTO(detail2)).thenReturn(response2);
        when(formulaDetailMapper.toResponseDTO(detail3)).thenReturn(response3);

        List<FormulaDetailResponseDTO> result = formulaDetailService.saveBulk(bulkRequest);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(3)).findById(any(UUID.class));
        verify(formulaDetailRepository, times(1)).saveAll(anyList());
        verify(formulaDetailMapper, times(3)).toResponseDTO(any(FormulaDetail.class));
    }

    @Test
    void saveBulk_Fails_FormulaNotFound() {

        UUID nonExistentFormulaId = UUID.randomUUID();
        List<FormulaMedicationDTO> medications = Arrays.asList(
            new FormulaMedicationDTO(medicationIds.get(0), 2, "Cada 8 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(nonExistentFormulaId, medications);

        when(formulaRepository.findById(nonExistentFormulaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si la fórmula no existe");

        verify(formulaRepository, times(1)).findById(nonExistentFormulaId);
        verify(formulaDetailRepository, never()).saveAll(anyList());
    }

    @Test
    void saveBulk_Fails_MedicationNotFound() {

        UUID formulaId = formulaIds.get(0);
        UUID nonExistentMedicationId = UUID.randomUUID();

        List<FormulaMedicationDTO> medications = Arrays.asList(
            new FormulaMedicationDTO(nonExistentMedicationId, 2, "Cada 8 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);
        
        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        when(medicationRepository.findById(nonExistentMedicationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si algún medicamento no existe");

        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(1)).findById(nonExistentMedicationId);
        verify(formulaDetailRepository, never()).saveAll(anyList());
    }

    @Test
    void saveBulk_Fails_MedicationAlreadyInFormula() {

        UUID formulaId = formulaIds.get(0);
        UUID existingMedicationId = medicationIds.get(0);

        List<FormulaMedicationDTO> medications = Arrays.asList(
            new FormulaMedicationDTO(existingMedicationId, 2, "Cada 8 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);
        
        Medication existingMedication = new Medication("Ibuprofeno", 15000);
        existingMedication.setIdMedication(existingMedicationId);
        
        FormulaDetail existingDetail = new FormulaDetail();
        existingDetail.setIdFormulaDetail(UUID.randomUUID());

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        when(medicationRepository.findById(existingMedicationId)).thenReturn(Optional.of(existingMedication));
        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, existingMedicationId))
            .thenReturn(Optional.of(existingDetail));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si algún medicamento ya está en la fórmula");

        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(1)).findById(existingMedicationId);
        verify(formulaDetailRepository, never()).saveAll(anyList());
    }

    @Test
    void saveBulk_Fails_DuplicateMedicationsInRequest() {

        UUID formulaId = formulaIds.get(0);
        UUID duplicateMedicationId = medicationIds.get(0);

        List<FormulaMedicationDTO> medications = Arrays.asList(
            new FormulaMedicationDTO(duplicateMedicationId, 2, "Cada 8 horas"),
            new FormulaMedicationDTO(duplicateMedicationId, 3, "Cada 6 horas") // Duplicado
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);
        
        Medication medication = new Medication("Ibuprofeno", 15000);
        medication.setIdMedication(duplicateMedicationId);

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        when(medicationRepository.findById(duplicateMedicationId)).thenReturn(Optional.of(medication));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si hay medicamentos duplicados en la misma petición");

        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(1)).findById(duplicateMedicationId);
        verify(formulaDetailRepository, never()).saveAll(anyList());
    }

    @Test
    void saveBulk_Success_EmptyList() {

        UUID formulaId = formulaIds.get(0);
        List<FormulaMedicationDTO> emptyMedications = Arrays.asList();

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, emptyMedications);

        assertThrows(ResourceNotFoundException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si la lista de medicamentos está vacía");

        verify(formulaDetailRepository, never()).saveAll(anyList());
    }
}