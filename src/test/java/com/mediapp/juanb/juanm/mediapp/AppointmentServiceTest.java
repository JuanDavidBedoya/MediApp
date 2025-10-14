package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.sql.Date;
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
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    private List<AppointmentRequestDTO> appointmentRequests;
    private List<Appointment> mockAppointments;
    private List<UUID> appointmentIds;
    private Date futureDate;
    private Time time;

    @BeforeEach
    void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        futureDate = new Date(cal.getTimeInMillis());
        time = new Time(9, 0, 0);

        // --- Dataset con 5 citas ---
        appointmentRequests = new ArrayList<>();
        mockAppointments = new ArrayList<>();
        appointmentIds = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            UUID id = UUID.randomUUID();
            appointmentIds.add(id);

            Doctor doctor = new Doctor();
            doctor.setCedulaDoctor("DOC-" + i);

            User patient = new User();
            patient.setCedula("PAT-" + i);

            AppointmentRequestDTO req = new AppointmentRequestDTO(
                    doctor.getCedulaDoctor(),
                    patient.getCedula(),
                    futureDate,
                    time,
                    "Observación #" + i
            );

            Appointment app = new Appointment();
            app.setIdAppointment(id);
            app.setDoctor(doctor);
            app.setPatient(patient);
            app.setDate(futureDate);
            app.setTime(time);
            app.setObservations("Observación #" + i);
            app.setFormulas(new ArrayList<>());

            appointmentRequests.add(req);
            mockAppointments.add(app);
        }
    }

    @Test
    void save_Success_NoConflict() {
        AppointmentRequestDTO request = appointmentRequests.get(0);
        Appointment appointment = mockAppointments.get(0);
        UUID id = appointment.getIdAppointment();

        when(appointmentRepository.findConflictingAppointment(anyString(), anyString(), any(Date.class), any(Time.class)))
                .thenReturn(Optional.empty());
        when(appointmentMapper.toEntity(request, null)).thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toResponseDTO(any(Appointment.class)))
                .thenReturn(new AppointmentResponseDTO(id, "DOC-1", "PAT-1", futureDate, time, "Observación #1"));

        AppointmentResponseDTO result = appointmentService.save(request);

        assertNotNull(result);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void save_Fails_DoctorConflict() {
        Appointment conflictApp = mockAppointments.get(1);

        when(appointmentRepository.findConflictingAppointment(anyString(), anyString(), any(Date.class), any(Time.class)))
                .thenReturn(Optional.of(conflictApp));

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.save(appointmentRequests.get(1));
        });

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void delete_Fails_HasAssociatedFormulas() {
        Appointment appointment = mockAppointments.get(2);
        Formula mockFormula = new Formula();
        appointment.setFormulas(Collections.singletonList(mockFormula));

        when(appointmentRepository.findById(appointment.getIdAppointment()))
                .thenReturn(Optional.of(appointment));

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.delete(appointment.getIdAppointment());
        });

        verify(appointmentRepository, never()).delete(any(Appointment.class));
    }
}