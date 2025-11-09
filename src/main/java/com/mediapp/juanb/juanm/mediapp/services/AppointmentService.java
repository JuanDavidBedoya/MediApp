package com.mediapp.juanb.juanm.mediapp.services;

import java.sql.Time;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.dtos.AppointmentRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.AppointmentResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.AppointmentMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.AppointmentRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    private void validateTimeConflict(String doctorCedula, String patientCedula, Date date, Time time, UUID currentAppointmentId) {

        Optional<Appointment> conflict = appointmentRepository.findConflictingAppointment(doctorCedula, patientCedula, date, time);

        conflict.ifPresent(app -> {
            if (currentAppointmentId == null || !app.getIdAppointment().equals(currentAppointmentId)) {
                if (app.getDoctor().getCedulaDoctor().equals(doctorCedula)) {
                    throw new IllegalArgumentException("El Doctor ya tiene una cita agendada para esa fecha y hora.");
                }
                if (app.getPatient().getCedula().equals(patientCedula)) {
                    throw new IllegalArgumentException("El Paciente ya tiene una cita agendada para esa fecha y hora.");
                }
            }
        });
    }

    public AppointmentResponseDTO save(AppointmentRequestDTO requestDTO) {

        validateTimeConflict(requestDTO.doctorCedula(), requestDTO.patientCedula(), requestDTO.date(), requestDTO.time(), null);

        Appointment appointment = appointmentMapper.toEntity(requestDTO, null);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDTO(savedAppointment);
    }

    public AppointmentResponseDTO update(UUID id, AppointmentRequestDTO requestDTO) {
        Appointment existingAppointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        validateTimeConflict(requestDTO.doctorCedula(), requestDTO.patientCedula(), requestDTO.date(), requestDTO.time(), id);

        Appointment updatedAppointment = appointmentMapper.toEntity(requestDTO, id);
        updatedAppointment.setIdAppointment(id);

        Appointment savedAppointment = appointmentRepository.save(updatedAppointment);
        return appointmentMapper.toResponseDTO(savedAppointment);
    }

    public AppointmentResponseDTO findById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
        return appointmentMapper.toResponseDTO(appointment);
    }

    public List<AppointmentResponseDTO> findAll() {
        return appointmentRepository.findAll().stream()
            .map(appointmentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> findByPatientCedula(String cedula) {
        return appointmentRepository.findByPatientCedula(cedula).stream()
            .map(appointmentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public void delete(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        if (!appointment.getFormulas().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar la cita porque ya tiene fórmulas médicas asociadas. Primero elimine las fórmulas.");
        }

        appointmentRepository.delete(appointment);
    }

    public List<AppointmentResponseDTO> findByDoctorCedula(String cedula) {
        return appointmentRepository.findByPatientCedula(cedula).stream()
            .map(appointmentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> findByDoctorCedulaAndDate(String cedula, java.time.LocalDate date) {
        Date sqlDate = Date.valueOf(date);
        return appointmentRepository.findByDoctorCedulaAndDate(cedula, sqlDate).stream()
            .map(appointmentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> findByDoctorCedulaAndDateRange(String cedula, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);
        return appointmentRepository.findByDoctorCedulaAndDateRange(cedula, sqlStartDate, sqlEndDate).stream()
            .map(appointmentMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
}