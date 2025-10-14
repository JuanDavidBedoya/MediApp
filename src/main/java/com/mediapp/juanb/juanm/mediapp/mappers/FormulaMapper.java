package com.mediapp.juanb.juanm.mediapp.mappers;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.repositories.AppointmentRepository;

@Component
public class FormulaMapper {
    
    private final AppointmentRepository appointmentRepository;

    public FormulaMapper(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Formula toEntity(FormulaRequestDTO dto) {
        if (dto == null) return null;
        Formula entity = new Formula();
        
        // Buscar y validar la existencia de la cita
        Appointment appointment = appointmentRepository.findById(dto.appointmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + dto.appointmentId()));
        entity.setAppointment(appointment);
        
        // Establecer la fecha actual de generación
        entity.setDate(new Date()); 
        return entity;
    }

    public FormulaResponseDTO toResponseDTO(Formula entity) {
        if (entity == null) return null;
        return new FormulaResponseDTO(
            entity.getIdFormula(),
            entity.getAppointment().getIdAppointment(),
            entity.getDate()
        );
    }
}