package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Mock
    private FormulaRepository formulaRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private FormulaDetailService formulaDetailService;

    private List<FormulaDetailRequestDTO> requestList;
    private List<FormulaDetail> mockDetails;
    private List<UUID> formulaIds;
    private List<String> medicationNames;

    @BeforeEach
    void setUp() {
        requestList = new ArrayList<>();
        mockDetails = new ArrayList<>();
        formulaIds = new ArrayList<>();
        medicationNames = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            UUID formulaId = UUID.randomUUID();
            String name = "Medicamento " + i;
            UUID detailId = UUID.randomUUID();

            formulaIds.add(formulaId);
            medicationNames.add(name);

            FormulaDetailRequestDTO req = new FormulaDetailRequestDTO(
                    formulaId,
                    name,
                    i * 2,
                    "Cada " + (6 + i * 2) + " horas"
            );
            requestList.add(req);

            FormulaDetail detail = new FormulaDetail();
            detail.setIdFormulaDetail(detailId);

            Formula formula = new Formula();
            formula.setIdFormula(formulaId);

            Medication medication = new Medication();
            medication.setName(name);

            detail.setFormula(formula);
            detail.setMedication(medication);
            detail.setQuantity(i * 2);
            detail.setDosage("Cada " + (6 + i * 2) + " horas");

            mockDetails.add(detail);
        }
    }

    @Test
    void update_Fails_ChangingImmutableFields() {
        FormulaDetail mockDetail = mockDetails.get(2);
        UUID detailId = mockDetail.getIdFormulaDetail();
        UUID formulaId = formulaIds.get(2);

        FormulaDetailRequestDTO invalidRequest = new FormulaDetailRequestDTO(
                formulaId,
                "Otro Medicamento",
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
    void save_Success() {
        UUID formulaId = formulaIds.get(0);
        String medicationName1 = medicationNames.get(0);
        String medicationName2 = medicationNames.get(1);
        String medicationName3 = medicationNames.get(2);

        List<FormulaMedicationDTO> medications = Arrays.asList(
                new FormulaMedicationDTO(medicationName1, 2, "Cada 8 horas"),
                new FormulaMedicationDTO(medicationName2, 1, "Cada 12 horas"),
                new FormulaMedicationDTO(medicationName3, 3, "Cada 6 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);

        Medication medication1 = new Medication(medicationName1, 15000);
        Medication medication2 = new Medication(medicationName2, 25000);
        Medication medication3 = new Medication(medicationName3, 8000);

        FormulaDetail detail1 = new FormulaDetail(mockFormula, medication1, 2, "Cada 8 horas");
        detail1.setIdFormulaDetail(UUID.randomUUID());
        FormulaDetail detail2 = new FormulaDetail(mockFormula, medication2, 1, "Cada 12 horas");
        detail2.setIdFormulaDetail(UUID.randomUUID());
        FormulaDetail detail3 = new FormulaDetail(mockFormula, medication3, 3, "Cada 6 horas");
        detail3.setIdFormulaDetail(UUID.randomUUID());

        List<FormulaDetail> savedDetails = Arrays.asList(detail1, detail2, detail3);

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        when(medicationRepository.findByName(medicationName1)).thenReturn(Optional.of(medication1));
        when(medicationRepository.findByName(medicationName2)).thenReturn(Optional.of(medication2));
        when(medicationRepository.findByName(medicationName3)).thenReturn(Optional.of(medication3));

        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationName(formulaId, medicationName1))
                .thenReturn(Optional.empty());
        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationName(formulaId, medicationName2))
                .thenReturn(Optional.empty());
        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationName(formulaId, medicationName3))
                .thenReturn(Optional.empty());

        when(formulaDetailRepository.saveAll(anyList())).thenReturn(savedDetails);

        List<FormulaDetailResponseDTO> result = formulaDetailService.saveBulk(bulkRequest);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(3)).findByName(anyString());
        verify(formulaDetailRepository, times(1)).saveAll(anyList());
    }

    @Test
    void saveBulk_Fails_FormulaNotFound() {
        UUID nonExistentFormulaId = UUID.randomUUID();
        List<FormulaMedicationDTO> medications = Arrays.asList(
                new FormulaMedicationDTO("Ibuprofeno", 2, "Cada 8 horas")
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
        String nonExistentMedicationName = "MedicamentoNoExiste";

        List<FormulaMedicationDTO> medications = Arrays.asList(
                new FormulaMedicationDTO(nonExistentMedicationName, 2, "Cada 8 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        when(medicationRepository.findByName(nonExistentMedicationName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si algún medicamento no existe");

        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(1)).findByName(nonExistentMedicationName);
        verify(formulaDetailRepository, never()).saveAll(anyList());
    }

    @Test
    void saveBulk_Fails_MedicationAlreadyInFormula() {
        UUID formulaId = formulaIds.get(0);
        String existingMedicationName = medicationNames.get(0);

        List<FormulaMedicationDTO> medications = Arrays.asList(
                new FormulaMedicationDTO(existingMedicationName, 2, "Cada 8 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);

        Medication existingMedication = new Medication(existingMedicationName, 15000);
        FormulaDetail existingDetail = new FormulaDetail();
        existingDetail.setIdFormulaDetail(UUID.randomUUID());

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        when(medicationRepository.findByName(existingMedicationName)).thenReturn(Optional.of(existingMedication));
        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationName(formulaId, existingMedicationName))
                .thenReturn(Optional.of(existingDetail));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si algún medicamento ya está en la fórmula");

        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(1)).findByName(existingMedicationName);
        verify(formulaDetailRepository, never()).saveAll(anyList());
    }

    @Test
    void saveBulk_Fails_DuplicateMedicationsInRequest() {
        UUID formulaId = formulaIds.get(0);
        String duplicateMedicationName = medicationNames.get(0);

        List<FormulaMedicationDTO> medications = Arrays.asList(
                new FormulaMedicationDTO(duplicateMedicationName, 2, "Cada 8 horas"),
                new FormulaMedicationDTO(duplicateMedicationName, 3, "Cada 6 horas")
        );

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, medications);

        Formula mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));
        when(medicationRepository.findByName(duplicateMedicationName))
                .thenReturn(Optional.of(new Medication(duplicateMedicationName, 10000)));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si hay medicamentos duplicados en la misma petición");

        verify(formulaRepository, times(1)).findById(formulaId);
        verify(medicationRepository, times(1)).findByName(duplicateMedicationName);
        verify(formulaDetailRepository, never()).saveAll(anyList());
    }

    @Test
    void saveBulk_Fails_EmptyList() {
        UUID formulaId = formulaIds.get(0);
        List<FormulaMedicationDTO> emptyMedications = List.of();

        FormulaDetailsBulkRequestDTO bulkRequest = new FormulaDetailsBulkRequestDTO(formulaId, emptyMedications);

        assertThrows(ResourceNotFoundException.class, () -> {
            formulaDetailService.saveBulk(bulkRequest);
        }, "Debe lanzar excepción si la lista de medicamentos está vacía");

        verify(formulaDetailRepository, never()).saveAll(anyList());
    }
}