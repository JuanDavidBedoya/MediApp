package com.mediapp.juanb.juanm.mediapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record SpecialityRequestDTO(
    @NotBlank(message = "El nombre de la especialidad no puede estar vacío")
    String name
) {}
