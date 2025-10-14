package com.mediapp.juanb.juanm.mediapp.dtos;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record FormulaRequestDTO(
    @NotNull(message = "El ID de la cita es obligatorio")
    UUID appointmentId,
    
    @NotNull(message = "La fecha de la cita es obligatoria")
    Date date,
    
    @NotEmpty(message = "La fórmula debe contener al menos un medicamento")
    List<FormulaMedicationDTO> medications
) {

        public FormulaRequestDTO(UUID appointmentId2, java.util.Date date2, List<FormulaMedicationDTO> medications2) {
            this(
                appointmentId2,
                date2 instanceof java.sql.Date ? (java.sql.Date) date2 : new java.sql.Date(date2.getTime()),
                medications2
            );
        }
    }