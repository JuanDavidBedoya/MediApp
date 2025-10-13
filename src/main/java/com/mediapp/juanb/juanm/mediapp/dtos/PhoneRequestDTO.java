package com.mediapp.juanb.juanm.mediapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhoneRequestDTO(
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Size(min = 7, max = 15, message = "El número de teléfono debe tener entre 7 y 15 caracteres")
    String phone
) {}