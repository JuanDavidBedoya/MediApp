package com.mediapp.juanb.juanm.mediapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MedicationRequestDTO(
    @NotBlank(message = "El nombre del medicamento no puede estar vacío")
    String name, 
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    Float price 
) {}
