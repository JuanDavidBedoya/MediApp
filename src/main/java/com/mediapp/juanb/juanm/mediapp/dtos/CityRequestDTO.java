package com.mediapp.juanb.juanm.mediapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record CityRequestDTO(
    @NotBlank(message = "El nombre de la ciudad no puede estar vacío")
    String name
) {}