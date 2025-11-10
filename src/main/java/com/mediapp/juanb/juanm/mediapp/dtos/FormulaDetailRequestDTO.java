package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FormulaDetailRequestDTO(
    
    @NotNull(message = "El ID de la formula es obligatorio")
    UUID formulaId, 
    
    @NotNull(message = "El ID del medicamento es obligatorio")
    String medicationId, 
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    int quantity, 
    
    @NotBlank(message = "La dosis es obligatoria")
    String dosage 
) {}
