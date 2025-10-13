package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.List;

public record UserResponseDTO(
        String cedula,
        String name,
        String email,
        String epsName,
        List<String> phones,
        String cityName
) {}
