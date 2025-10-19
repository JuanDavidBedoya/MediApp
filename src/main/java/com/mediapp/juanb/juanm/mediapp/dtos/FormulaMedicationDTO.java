package com.mediapp.juanb.juanm.mediapp.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FormulaMedicationDTO(
    @NotNull(message = "El nombre del medicamento es obligatorio")
    String name,
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    int quantity,
    
    @NotBlank(message = "La dosis es obligatoria")
    String dosage
) {}