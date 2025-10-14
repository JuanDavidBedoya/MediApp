package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

import org.hibernate.validator.constraints.NotBlank;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FormulaMedicationDTO(
    @NotNull(message = "El ID del medicamento es obligatorio")
    UUID medicationId,
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    int quantity,
    
    @NotBlank(message = "La dosis es obligatoria")
    String dosage
) {}
