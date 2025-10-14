package com.mediapp.juanb.juanm.mediapp.dtos;

import java.sql.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record FormulaRequestDTO(

    @NotNull(message = "El ID de la cita es obligatorio")
    UUID appointmentId,
    @NotNull(message = "La fecha de la cita es obligatoria")
    Date date
) {}