package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.mappers.FormulaMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaRepository;
import com.mediapp.juanb.juanm.mediapp.services.FormulaService;

@ExtendWith(MockitoExtension.class)
class FormulaServiceTest {

    @Mock
    private FormulaRepository formulaRepository;

    @Mock
    private FormulaMapper formulaMapper;

    @InjectMocks
    private FormulaService formulaService;

    private List<FormulaRequestDTO> requestList;
    private List<Formula> mockFormulas;
    private List<UUID> formulaIds;
    private List<UUID> appointmentIds;

    @BeforeEach
    void setUp() {
        requestList = new ArrayList<>();
        mockFormulas = new ArrayList<>();
        formulaIds = new ArrayList<>();
        appointmentIds = new ArrayList<>();

        // Dataset con 5 registros
        for (int i = 1; i <= 5; i++) {
            UUID formulaId = UUID.randomUUID();
            UUID appointmentId = UUID.randomUUID();
            Date date = new Date(System.currentTimeMillis() - (i * 86400000L)); // fecha distinta por registro

            FormulaRequestDTO request = new FormulaRequestDTO(appointmentId, null);
            requestList.add(request);

            Appointment mockAppointment = new Appointment();
            mockAppointment.setIdAppointment(appointmentId);

            Formula formula = new Formula();
            formula.setIdFormula(formulaId);
            formula.setAppointment(mockAppointment);
            formula.setDate(date);

            mockFormulas.add(formula);
            formulaIds.add(formulaId);
            appointmentIds.add(appointmentId);
        }
    }

    @Test
    void save_Success() {
        UUID appointmentId = appointmentIds.get(0);
        Formula mockFormula = mockFormulas.get(0);
        FormulaRequestDTO validRequest = requestList.get(0);
        UUID formulaId = mockFormula.getIdFormula();

        when(formulaRepository.findByAppointmentIdAppointment(appointmentId))
                .thenReturn(Optional.empty());
        when(formulaMapper.toEntity(validRequest))
                .thenReturn(mockFormula);
        when(formulaRepository.save(any(Formula.class)))
                .thenReturn(mockFormula);
        when(formulaMapper.toResponseDTO(any(Formula.class)))
                .thenReturn(new FormulaResponseDTO(formulaId, appointmentId, mockFormula.getDate()));

        FormulaResponseDTO result = formulaService.save(validRequest);

        assertNotNull(result);
        verify(formulaRepository, times(1)).save(any(Formula.class));
    }

    @Test
    void save_Fails_AppointmentAlreadyHasFormula() {
        UUID appointmentId = appointmentIds.get(1);
        Formula mockFormula = mockFormulas.get(1);
        FormulaRequestDTO validRequest = requestList.get(1);

        when(formulaRepository.findByAppointmentIdAppointment(appointmentId))
                .thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.save(validRequest);
        }, "Debe lanzar excepción si la cita ya tiene fórmula.");

        verify(formulaRepository, never()).save(any(Formula.class));
    }

    @Test
    void update_Fails_ChangingAppointmentId() {
        UUID formulaId = formulaIds.get(2);
        UUID appointmentId = appointmentIds.get(2);
        Formula mockFormula = mockFormulas.get(2);

        FormulaRequestDTO invalidUpdateRequest = new FormulaRequestDTO(UUID.randomUUID(), null);
        when(formulaRepository.findById(formulaId))
                .thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.update(formulaId, invalidUpdateRequest);
        }, "No debe permitir cambiar el ID de la cita.");

        verify(formulaRepository, never()).save(any(Formula.class));
    }

    @Test
    void delete_Fails_HasAssociatedDetails() {
        UUID formulaId = formulaIds.get(3);
        Formula mockFormula = mockFormulas.get(3);
        FormulaDetail mockDetail = new FormulaDetail();

        mockFormula.setFormulaDetails(Arrays.asList(mockDetail));
        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.delete(formulaId);
        }, "Debe lanzar excepción si la fórmula tiene detalles.");

        verify(formulaRepository, never()).delete(any(Formula.class));
    }
}