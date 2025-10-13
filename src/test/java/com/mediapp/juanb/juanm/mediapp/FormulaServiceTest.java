package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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

    private FormulaRequestDTO validRequest;
    private Formula mockFormula;
    private UUID formulaId;
    private UUID appointmentId;

    @BeforeEach
    void setUp() {
        formulaId = UUID.randomUUID();
        appointmentId = UUID.randomUUID();

        validRequest = new FormulaRequestDTO(appointmentId, null);

        Appointment mockAppointment = new Appointment();
        mockAppointment.setIdAppointment(appointmentId);

        mockFormula = new Formula();
        mockFormula.setIdFormula(formulaId);
        mockFormula.setAppointment(mockAppointment);
        mockFormula.setFormulaDetails(Collections.emptyList());
    }

    @Test
    void save_Success() {

        when(formulaRepository.findByAppointmentIdAppointment(appointmentId)).thenReturn(Optional.empty());
        when(formulaMapper.toEntity(validRequest)).thenReturn(mockFormula);
        when(formulaRepository.save(any(Formula.class))).thenReturn(mockFormula);
        when(formulaMapper.toResponseDTO(any(Formula.class))).thenReturn(new FormulaResponseDTO(formulaId, appointmentId, new Date()));

        FormulaResponseDTO result = formulaService.save(validRequest);

        assertNotNull(result);
        verify(formulaRepository, times(1)).save(any(Formula.class));
    }

    @Test
    void save_Fails_AppointmentAlreadyHasFormula() {

        when(formulaRepository.findByAppointmentIdAppointment(appointmentId)).thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.save(validRequest);
        }, "Debe lanzar excepción si la cita ya tiene fórmula.");
        verify(formulaRepository, never()).save(any(Formula.class));
    }

    @Test
    void update_Fails_ChangingAppointmentId() {

        UUID newAppointmentId = UUID.randomUUID();
        FormulaRequestDTO invalidUpdateRequest = new FormulaRequestDTO(newAppointmentId, null);
        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.update(formulaId, invalidUpdateRequest);
        }, "No debe permitir cambiar el ID de la cita.");
        verify(formulaRepository, never()).save(any(Formula.class));
    }

    @Test
    void delete_Fails_HasAssociatedDetails() {

        FormulaDetail mockDetail = new FormulaDetail();
        mockFormula.setFormulaDetails(Arrays.asList(mockDetail));
        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.delete(formulaId);
        }, "Debe lanzar excepción si la fórmula tiene detalles.");
        verify(formulaRepository, never()).delete(any(Formula.class));
    }
}