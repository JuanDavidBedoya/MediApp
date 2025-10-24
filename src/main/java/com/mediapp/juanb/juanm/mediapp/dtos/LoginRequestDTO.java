package com.mediapp.juanb.juanm.mediapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "La cédula no puede estar vacía")
    String cedula,

    @NotBlank(message = "La contraseña no puede estar vacía")
    String password
) {}