package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
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
import com.mediapp.juanb.juanm.mediapp.mappers.MedicationMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;
import com.mediapp.juanb.juanm.mediapp.services.MedicationService;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    private Medication existingMedication;
    private MedicationRequestDTO validRequest;
    private UUID existingId;

    @BeforeEach
    void setUp() {
        existingId = UUID.randomUUID();

        existingMedication = new Medication();
        existingMedication.setIdMedication(existingId);
        existingMedication.setName("Aspirina");
        existingMedication.setPrice(5.0f);
        existingMedication.setFormulaDetails(Collections.emptyList());

        validRequest = new MedicationRequestDTO("Ibuprofeno", 8.0f);
    }

    @Test
    void save_Success() {

        Medication newMedication = MedicationMapper.toEntity(validRequest);
        when(medicationRepository.findByName(validRequest.name())).thenReturn(Optional.empty());
        when(medicationRepository.save(any(Medication.class))).thenReturn(existingMedication); 

        MedicationResponseDTO result = medicationService.save(validRequest);

        assertNotNull(result);
        assertEquals(existingMedication.getName(), result.name());
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    void save_Fails_NameAlreadyExists() {

        when(medicationRepository.findByName(validRequest.name())).thenReturn(Optional.of(existingMedication));

        assertThrows(IllegalArgumentException.class, () -> {
            medicationService.save(validRequest);
        }, "Debe lanzar excepción si el nombre ya existe.");
        verify(medicationRepository, never()).save(any(Medication.class));
    }

    @Test
    void update_Success() {

        MedicationRequestDTO updateRequest = new MedicationRequestDTO("Aspirina Modificada", 10.0f);
        when(medicationRepository.findById(existingId)).thenReturn(Optional.of(existingMedication));
        when(medicationRepository.findByName(updateRequest.name())).thenReturn(Optional.empty()); 
        when(medicationRepository.save(any(Medication.class))).thenReturn(existingMedication); 

        MedicationResponseDTO result = medicationService.update(existingId, updateRequest);

        assertNotNull(result);
        assertEquals("Aspirina Modificada", existingMedication.getName()); 
    }

    @Test
    void delete_Success_NoFormulaDetails() {

        when(medicationRepository.findById(existingId)).thenReturn(Optional.of(existingMedication));
    
        medicationService.delete(existingId);

        verify(medicationRepository, times(1)).delete(existingMedication);
    }

    @Test
    void delete_Fails_InUseByFormulaDetails() {

        FormulaDetail mockDetail = new FormulaDetail();
        existingMedication.setFormulaDetails(Arrays.asList(mockDetail));
        when(medicationRepository.findById(existingId)).thenReturn(Optional.of(existingMedication));

        assertThrows(IllegalArgumentException.class, () -> {
            medicationService.delete(existingId);
        }, "Debe lanzar excepción si el medicamento está en uso.");
        verify(medicationRepository, never()).delete(any(Medication.class));
    }
}
