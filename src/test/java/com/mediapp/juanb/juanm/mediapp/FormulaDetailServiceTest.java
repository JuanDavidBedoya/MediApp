package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.mappers.FormulaDetailMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaDetailRepository;
import com.mediapp.juanb.juanm.mediapp.services.FormulaDetailService;

@ExtendWith(MockitoExtension.class)
class FormulaDetailServiceTest {

    @Mock
    private FormulaDetailRepository formulaDetailRepository;

    @Mock
    private FormulaDetailMapper formulaDetailMapper;

    @InjectMocks
    private FormulaDetailService formulaDetailService;

    private FormulaDetailRequestDTO validRequest;
    private FormulaDetail mockDetail;
    private UUID formulaId;
    private UUID medicationId;

    @BeforeEach
    void setUp() {
        formulaId = UUID.randomUUID();
        medicationId = UUID.randomUUID();

        validRequest = new FormulaDetailRequestDTO(formulaId, medicationId, 2, "Cada 8 horas");
   
        mockDetail = new FormulaDetail();
        mockDetail.setIdFormulaDetail(UUID.randomUUID());
    }

    @Test
    void save_Success() {

        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, medicationId)).thenReturn(Optional.empty());
        when(formulaDetailMapper.toEntity(validRequest)).thenReturn(mockDetail);
        when(formulaDetailRepository.save(any(FormulaDetail.class))).thenReturn(mockDetail);
        when(formulaDetailMapper.toResponseDTO(any(FormulaDetail.class))).thenReturn(new FormulaDetailResponseDTO(mockDetail.getIdFormulaDetail(), formulaId, medicationId, 2, "Cada 8 horas"));

        FormulaDetailResponseDTO result = formulaDetailService.save(validRequest);

        assertNotNull(result);
        verify(formulaDetailRepository, times(1)).save(any(FormulaDetail.class));
    }

    @Test
    void save_Fails_MedicationAlreadyInFormula() {

        when(formulaDetailRepository.findByFormulaIdFormulaAndMedicationIdMedication(formulaId, medicationId)).thenReturn(Optional.of(mockDetail));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.save(validRequest);
        }, "Debe lanzar excepción si el medicamento ya está en la fórmula.");
        verify(formulaDetailRepository, never()).save(any(FormulaDetail.class));
    }

    @Test
    void update_Fails_ChangingImmutableFields() {

        UUID detailId = mockDetail.getIdFormulaDetail();
        
        Formula mockFormula = new Formula(); mockFormula.setIdFormula(formulaId);
        Medication mockMedication = new Medication(); mockMedication.setIdMedication(medicationId);
        mockDetail.setFormula(mockFormula);
        mockDetail.setMedication(mockMedication);

        FormulaDetailRequestDTO invalidRequest = new FormulaDetailRequestDTO(formulaId, UUID.randomUUID(), 5, "Dos veces al día");
        
        when(formulaDetailRepository.findById(detailId)).thenReturn(Optional.of(mockDetail));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.update(detailId, invalidRequest);
        }, "No debe permitir cambiar la fórmula o el medicamento.");
        verify(formulaDetailRepository, never()).save(any(FormulaDetail.class));
    }
}
