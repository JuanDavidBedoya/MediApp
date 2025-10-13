package com.mediapp.juanb.juanm.mediapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record EpsRequestDTO(
    @NotBlank(message = "El nombre de la EPS no puede estar vacío")
    String name
) {}