package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
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

import com.mediapp.juanb.juanm.mediapp.dtos.AppointmentRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.AppointmentResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.mappers.AppointmentMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.AppointmentRepository;
import com.mediapp.juanb.juanm.mediapp.services.AppointmentService;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper; // Mappeo de DTO a Entidad

    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentRequestDTO validRequest;
    private Appointment mockAppointment;
    private UUID appointmentId;
    private Date futureDate;
    private Date time;

    @BeforeEach
    void setUp() {
        appointmentId = UUID.randomUUID();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        futureDate = cal.getTime();
        time = new Date();

        validRequest = new AppointmentRequestDTO("123456", "654321", futureDate, time, "Chequeo general");

        Doctor mockDoctor = new Doctor(); mockDoctor.setCedulaDoctor("123456");
        User mockPatient = new User(); mockPatient.setCedula("654321");

        mockAppointment = new Appointment();
        mockAppointment.setIdAppointment(appointmentId);
        mockAppointment.setDoctor(mockDoctor);
        mockAppointment.setPatient(mockPatient);
        mockAppointment.setFormulas(Collections.emptyList());
    }

    @Test
    void save_Success_NoConflict() {

        when(appointmentRepository.findConflictingAppointment(anyString(), anyString(), any(Date.class), any(Date.class)))
            .thenReturn(Optional.empty());
        when(appointmentMapper.toEntity(validRequest, null)).thenReturn(mockAppointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);
        when(appointmentMapper.toResponseDTO(any(Appointment.class))).thenReturn(new AppointmentResponseDTO(appointmentId, "123456", "654321", futureDate, time, "Chequeo general"));

        AppointmentResponseDTO result = appointmentService.save(validRequest);

        assertNotNull(result);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void save_Fails_DoctorConflict() {

        Appointment conflictApp = mockAppointment;

        when(appointmentRepository.findConflictingAppointment(anyString(), anyString(), any(Date.class), any(Date.class)))
            .thenReturn(Optional.of(conflictApp));

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.save(validRequest);
        }, "Debe lanzar excepción por conflicto de horario.");
        
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void delete_Fails_HasAssociatedFormulas() {

        Formula mockFormula = new Formula();
        mockAppointment.setFormulas(Arrays.asList(mockFormula));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(mockAppointment));

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.delete(appointmentId);
        }, "Debe lanzar excepción si la cita tiene fórmulas.");
        verify(appointmentRepository, never()).delete(any(Appointment.class));
    }
}