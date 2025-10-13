package com.mediapp.juanb.juanm.mediapp.mappers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mediapp.juanb.juanm.mediapp.dtos.AppointmentRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.AppointmentResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.repositories.AppointmentRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.DoctorRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.UserRepository;

@Component
public class AppointmentMapper {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentMapper(DoctorRepository doctorRepository, UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment toEntity(AppointmentRequestDTO dto, UUID id) {
        if (dto == null) return null;

        Appointment entity = id == null
                ? new Appointment()
                : appointmentRepository.findById(id).orElse(new Appointment());

        if (id != null) entity.setIdAppointment(id);

        Doctor doctor = doctorRepository.findById(dto.doctorCedula())
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado: " + dto.doctorCedula()));
        entity.setDoctor(doctor);
        
        User patient = userRepository.findById(dto.patientCedula())
            .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado: " + dto.patientCedula()));
        entity.setPatient(patient);

        entity.setDate(dto.date());
        entity.setTime(dto.time());
        entity.setObservations(dto.observations());

        return entity;
    }

    public AppointmentResponseDTO toResponseDTO(Appointment entity) {
        if (entity == null) return null;
        return new AppointmentResponseDTO(
            entity.getIdAppointment(),
            entity.getDoctor().getCedulaDoctor(),
            entity.getPatient().getCedula(),
            entity.getDate(),
            entity.getTime(),
            entity.getObservations()
        );
    }
}