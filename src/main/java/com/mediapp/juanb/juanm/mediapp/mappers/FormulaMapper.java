package com.mediapp.juanb.juanm.mediapp.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.repositories.AppointmentRepository;

@Component
public class FormulaMapper {
    
    private final AppointmentRepository appointmentRepository;
    private final FormulaDetailMapper formulaDetailMapper;

    public FormulaMapper(AppointmentRepository appointmentRepository, FormulaDetailMapper formulaDetailMapper) {
        this.appointmentRepository = appointmentRepository;
        this.formulaDetailMapper = formulaDetailMapper;
    }

    public Formula toEntity(FormulaRequestDTO dto) {
        if (dto == null) return null;
        Formula entity = new Formula();
        
        // Buscar y validar la existencia de la cita
        Appointment appointment = appointmentRepository.findById(dto.appointmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + dto.appointmentId()));
        entity.setAppointment(appointment);
        
        entity.setDate(dto.date()); // Usar la fecha del DTO
        return entity;
    }

    public FormulaResponseDTO toResponseDTO(Formula entity) {
        if (entity == null) return null;
        
        // Convertir los detalles de la fórmula
        List<FormulaDetailResponseDTO> medicationDetails = entity.getFormulaDetails().stream()
            .map(formulaDetailMapper::toResponseDTO)
            .collect(Collectors.toList());
        
        return new FormulaResponseDTO(
            entity.getIdFormula(),
            entity.getAppointment().getIdAppointment(),
            entity.getDate(),
            medicationDetails  // ← Incluir los medicamentos
        );
    }
}