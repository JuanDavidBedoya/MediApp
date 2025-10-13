package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPhoneRequestDTO(
    @NotBlank(message = "La cédula del usuario es obligatoria")
    String userCedula,

    @NotNull(message = "El ID del teléfono es obligatorio")
    UUID phoneId
) {}