package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaMedicationDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.FormulaMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.MedicationRepository;
import com.mediapp.juanb.juanm.mediapp.services.FormulaService;

@ExtendWith(MockitoExtension.class)
class FormulaServiceTest {

    @Mock
    private FormulaRepository formulaRepository;

    @Mock
    private FormulaMapper formulaMapper;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private FormulaService formulaService;

    private List<FormulaRequestDTO> requestList;
    private List<Formula> mockFormulas;
    private List<UUID> formulaIds;
    private List<UUID> appointmentIds;
    private List<FormulaMedicationDTO> mockMedications;

    @BeforeEach
    void setUp() {
        requestList = new ArrayList<>();
        mockFormulas = new ArrayList<>();
        formulaIds = new ArrayList<>();
        appointmentIds = new ArrayList<>();
        mockMedications = new ArrayList<>();

        // Dataset con 5 registros
        for (int i = 1; i <= 5; i++) {
            UUID formulaId = UUID.randomUUID();
            UUID appointmentId = UUID.randomUUID();
            UUID medicationId = UUID.randomUUID();
            Date date = new Date(System.currentTimeMillis() - (i * 86400000L));

            // Crear lista de medicamentos para cada fórmula
            List<FormulaMedicationDTO> medications = Arrays.asList(
                new FormulaMedicationDTO(medicationId, i, "Dosis " + i)
            );

            FormulaRequestDTO request = new FormulaRequestDTO(appointmentId, date, medications);
            requestList.add(request);

            Appointment mockAppointment = new Appointment();
            mockAppointment.setIdAppointment(appointmentId);

            Formula formula = new Formula();
            formula.setIdFormula(formulaId);
            formula.setAppointment(mockAppointment);
            formula.setDate(date);
            formula.setFormulaDetails(new ArrayList<>());

            // Crear detalle de fórmula
            Medication medication = new Medication("Medicamento " + i, 1000 * i);
            medication.setIdMedication(medicationId);
            
            FormulaDetail detail = new FormulaDetail(formula, medication, i, "Dosis " + i);
            formula.getFormulaDetails().add(detail);

            mockFormulas.add(formula);
            formulaIds.add(formulaId);
            appointmentIds.add(appointmentId);
            mockMedications.add(new FormulaMedicationDTO(medicationId, i, "Dosis " + i));
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
        
        // Mock para cada medicamento en la lista
        for (FormulaMedicationDTO medDTO : validRequest.medications()) {
            Medication medication = new Medication("Test Medication", 1000);
            medication.setIdMedication(medDTO.medicationId());
            when(medicationRepository.findById(medDTO.medicationId()))
                    .thenReturn(Optional.of(medication));
        }
        
        when(formulaRepository.save(any(Formula.class)))
                .thenReturn(mockFormula);
        
        // Mock para la respuesta que ahora incluye medicamentos
        List<FormulaDetailResponseDTO> medicationDetails = mockFormula.getFormulaDetails().stream()
                .map(detail -> new FormulaDetailResponseDTO(
                    detail.getIdFormulaDetail(),
                    detail.getFormula().getIdFormula(),
                    detail.getMedication().getIdMedication(),
                    detail.getQuantity(),
                    detail.getDosage()
                ))
                .collect(Collectors.toList());
                
        when(formulaMapper.toResponseDTO(any(Formula.class)))
                .thenReturn(new FormulaResponseDTO(formulaId, appointmentId, mockFormula.getDate(), medicationDetails));

        FormulaResponseDTO result = formulaService.save(validRequest);

        assertNotNull(result);
        assertEquals(appointmentId, result.appointmentId());
        assertFalse(result.medications().isEmpty());
        verify(formulaRepository, times(1)).save(any(Formula.class));
        verify(medicationRepository, times(validRequest.medications().size())).findById(any(UUID.class));
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
        verify(medicationRepository, never()).findById(any(UUID.class));
    }

    @Test
    void save_Fails_MedicationNotFound() {
        UUID appointmentId = appointmentIds.get(2);
        FormulaRequestDTO validRequest = requestList.get(2);
        UUID nonExistentMedicationId = UUID.randomUUID();

        // Crear request con medicamento que no existe
        List<FormulaMedicationDTO> medicationsWithInvalid = Arrays.asList(
            new FormulaMedicationDTO(nonExistentMedicationId, 1, "Dosis test")
        );
        FormulaRequestDTO invalidRequest = new FormulaRequestDTO(
            validRequest.appointmentId(), 
            validRequest.date(), 
            medicationsWithInvalid
        );

        when(formulaRepository.findByAppointmentIdAppointment(appointmentId))
                .thenReturn(Optional.empty());
        when(formulaMapper.toEntity(invalidRequest))
                .thenReturn(mockFormulas.get(2));
        when(medicationRepository.findById(nonExistentMedicationId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            formulaService.save(invalidRequest);
        }, "Debe lanzar excepción si el medicamento no existe.");

        verify(formulaRepository, never()).save(any(Formula.class));
    }

    @Test
    void update_Fails_ChangingAppointmentId() {
        UUID formulaId = formulaIds.get(3);
        UUID appointmentId = appointmentIds.get(3);
        Formula mockFormula = mockFormulas.get(3);

        // Crear request con appointmentId diferente
        FormulaRequestDTO invalidUpdateRequest = new FormulaRequestDTO(
            UUID.randomUUID(), 
            mockFormula.getDate(), 
            Arrays.asList()
        );
        
        when(formulaRepository.findById(formulaId))
                .thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.update(formulaId, invalidUpdateRequest);
        }, "No debe permitir cambiar el ID de la cita.");

        verify(formulaRepository, never()).save(any(Formula.class));
    }

    @Test
    void update_Success() {
        UUID formulaId = formulaIds.get(4);
        UUID appointmentId = appointmentIds.get(4);
        Formula mockFormula = mockFormulas.get(4);

        FormulaRequestDTO validUpdateRequest = new FormulaRequestDTO(
            appointmentId, 
            new Date(), // Nueva fecha
            Arrays.asList() // Sin cambios en medicamentos para update
        );
        
        when(formulaRepository.findById(formulaId))
                .thenReturn(Optional.of(mockFormula));
        when(formulaRepository.save(any(Formula.class)))
                .thenReturn(mockFormula);
        
        // Mock para la respuesta
        List<FormulaDetailResponseDTO> medicationDetails = mockFormula.getFormulaDetails().stream()
                .map(detail -> new FormulaDetailResponseDTO(
                    detail.getIdFormulaDetail(),
                    detail.getFormula().getIdFormula(),
                    detail.getMedication().getIdMedication(),
                    detail.getQuantity(),
                    detail.getDosage()
                ))
                .collect(Collectors.toList());
                
        when(formulaMapper.toResponseDTO(any(Formula.class)))
                .thenReturn(new FormulaResponseDTO(formulaId, appointmentId, validUpdateRequest.date(), medicationDetails));

        FormulaResponseDTO result = formulaService.update(formulaId, validUpdateRequest);

        assertNotNull(result);
        assertEquals(validUpdateRequest.date(), result.date());
        verify(formulaRepository, times(1)).save(any(Formula.class));
    }

    @Test
    void delete_Fails_HasAssociatedDetails() {
        UUID formulaId = formulaIds.get(0);
        Formula mockFormula = mockFormulas.get(0);

        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));

        assertThrows(IllegalArgumentException.class, () -> {
            formulaService.delete(formulaId);
        }, "Debe lanzar excepción si la fórmula tiene detalles.");

        verify(formulaRepository, never()).delete(any(Formula.class));
    }

    @Test
    void delete_Success() {
        UUID formulaId = formulaIds.get(1);
        Formula mockFormula = mockFormulas.get(1);
        
        // Crear fórmula sin detalles
        mockFormula.setFormulaDetails(new ArrayList<>());
        
        when(formulaRepository.findById(formulaId)).thenReturn(Optional.of(mockFormula));

        assertDoesNotThrow(() -> {
            formulaService.delete(formulaId);
        }, "Debe eliminar la fórmula si no tiene detalles.");

        verify(formulaRepository, times(1)).delete(mockFormula);
    }

    @Test
    void findById_Success() {
        UUID formulaId = formulaIds.get(2);
        Formula mockFormula = mockFormulas.get(2);

        when(formulaRepository.findByIdWithDetails(formulaId))
            .thenReturn(Optional.of(mockFormula));
        
        List<FormulaDetailResponseDTO> medicationDetails = mockFormula.getFormulaDetails().stream()
                .map(detail -> new FormulaDetailResponseDTO(
                    detail.getIdFormulaDetail(),
                    detail.getFormula().getIdFormula(),
                    detail.getMedication().getIdMedication(),
                    detail.getQuantity(),
                    detail.getDosage()
                ))
                .collect(Collectors.toList());
                
        when(formulaMapper.toResponseDTO(mockFormula))
                .thenReturn(new FormulaResponseDTO(formulaId, mockFormula.getAppointment().getIdAppointment(), mockFormula.getDate(), medicationDetails));

        FormulaResponseDTO result = formulaService.findById(formulaId);

        assertNotNull(result);
        assertEquals(formulaId, result.idFormula());
        assertFalse(result.medications().isEmpty());
    }

    @Test
    void findAll_Success() {
        when(formulaRepository.findAll())
                .thenReturn(mockFormulas);
        
        // Mock para cada conversión
        for (int i = 0; i < mockFormulas.size(); i++) {
            Formula formula = mockFormulas.get(i);
            List<FormulaDetailResponseDTO> medicationDetails = formula.getFormulaDetails().stream()
                    .map(detail -> new FormulaDetailResponseDTO(
                        detail.getIdFormulaDetail(),
                        detail.getFormula().getIdFormula(),
                        detail.getMedication().getIdMedication(),
                        detail.getQuantity(),
                        detail.getDosage()
                    ))
                    .collect(Collectors.toList());
                    
            when(formulaMapper.toResponseDTO(formula))
                    .thenReturn(new FormulaResponseDTO(
                        formula.getIdFormula(),
                        formula.getAppointment().getIdAppointment(),
                        formula.getDate(),
                        medicationDetails
                    ));
        }

        List<FormulaResponseDTO> result = formulaService.findAll();

        assertNotNull(result);
        assertEquals(mockFormulas.size(), result.size());
        verify(formulaMapper, times(mockFormulas.size())).toResponseDTO(any(Formula.class));
    }
}