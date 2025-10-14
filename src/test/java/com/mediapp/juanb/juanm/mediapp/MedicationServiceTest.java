package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mediapp.juanb.juanm.mediapp.dtos.MedicationRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.MedicationResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;
import com.mediapp.juanb.juanm.mediapp.services.MedicationService;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    private List<Medication> mockMedications;
    private List<MedicationRequestDTO> requestList;
    private List<UUID> medicationIds;

    @BeforeEach
    void setUp() {
        mockMedications = new ArrayList<>();
        requestList = new ArrayList<>();
        medicationIds = new ArrayList<>();

        // Crear dataset con 5 medicamentos
        for (int i = 1; i <= 5; i++) {
            UUID id = UUID.randomUUID();
            medicationIds.add(id);

            Medication med = new Medication();
            med.setIdMedication(id);
            med.setName("Medicamento" + i);
            med.setPrice(5.0f * i);
            med.setFormulaDetails(Collections.emptyList());

            MedicationRequestDTO req = new MedicationRequestDTO("Medicamento" + i, 5.0f * i);

            mockMedications.add(med);
            requestList.add(req);
        }
    }

    @Test
    void save_Success() {
        MedicationRequestDTO validRequest = requestList.get(0);
        Medication newMedication = mockMedications.get(0);

        when(medicationRepository.findByName(validRequest.name()))
                .thenReturn(Optional.empty());
        when(medicationRepository.save(any(Medication.class)))
                .thenReturn(newMedication);

        MedicationResponseDTO result = medicationService.save(validRequest);

        assertNotNull(result);
        assertEquals(validRequest.name(), result.name());
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    void save_Fails_NameAlreadyExists() {
        Medication existingMedication = mockMedications.get(1);
        MedicationRequestDTO validRequest = requestList.get(1);

        when(medicationRepository.findByName(validRequest.name()))
                .thenReturn(Optional.of(existingMedication));

        assertThrows(IllegalArgumentException.class, () -> {
            medicationService.save(validRequest);
        }, "Debe lanzar excepción si el nombre ya existe.");

        verify(medicationRepository, never()).save(any(Medication.class));
    }

    @Test
    void update_Success() {
        Medication existingMedication = mockMedications.get(2);
        UUID existingId = existingMedication.getIdMedication();

        MedicationRequestDTO updateRequest = new MedicationRequestDTO("NuevoNombre", 12.0f);

        when(medicationRepository.findById(existingId))
                .thenReturn(Optional.of(existingMedication));
        when(medicationRepository.findByName(updateRequest.name()))
                .thenReturn(Optional.empty());
        when(medicationRepository.save(any(Medication.class)))
                .thenReturn(existingMedication);

        MedicationResponseDTO result = medicationService.update(existingId, updateRequest);

        assertNotNull(result);
        assertEquals("NuevoNombre", existingMedication.getName());
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    void delete_Success_NoFormulaDetails() {
        Medication existingMedication = mockMedications.get(3);
        UUID existingId = existingMedication.getIdMedication();

        when(medicationRepository.findById(existingId))
                .thenReturn(Optional.of(existingMedication));

        medicationService.delete(existingId);

        verify(medicationRepository, times(1)).delete(existingMedication);
    }

    @Test
    void delete_Fails_InUseByFormulaDetails() {
        Medication existingMedication = mockMedications.get(4);
        UUID existingId = existingMedication.getIdMedication();

        FormulaDetail mockDetail = new FormulaDetail();
        existingMedication.setFormulaDetails(Arrays.asList(mockDetail));

        when(medicationRepository.findById(existingId))
                .thenReturn(Optional.of(existingMedication));

        assertThrows(IllegalArgumentException.class, () -> {
            medicationService.delete(existingId);
        }, "Debe lanzar excepción si el medicamento está en uso.");

        verify(medicationRepository, never()).delete(any(Medication.class));
    }
}