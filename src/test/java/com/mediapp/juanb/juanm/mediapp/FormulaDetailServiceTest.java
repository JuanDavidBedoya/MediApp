package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

        // --- Dataset con 5 fórmulas y 5 medicamentos ---
        for (int i = 1; i <= 5; i++) {
            UUID formulaId = UUID.randomUUID();
            UUID medicationId = UUID.randomUUID();
            UUID detailId = UUID.randomUUID();

            formulaIds.add(formulaId);
            medicationIds.add(medicationId);

            FormulaDetailRequestDTO req = new FormulaDetailRequestDTO(
                    formulaId,
                    medicationId,
                    i * 2, // cantidad distinta por registro
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
            detail.setDosage("Cada " + (6 + i * 2) + " horas"); // ✅ correcto tipo String

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
                UUID.randomUUID(), // cambia medicamento → debe fallar
                10,
                "Dos veces al día"
        );

        when(formulaDetailRepository.findById(detailId)).thenReturn(Optional.of(mockDetail));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaDetailService.update(detailId, invalidRequest);
        }, "No debe permitir cambiar la fórmula o el medicamento.");

        verify(formulaDetailRepository, never()).save(any(FormulaDetail.class));
    }
}