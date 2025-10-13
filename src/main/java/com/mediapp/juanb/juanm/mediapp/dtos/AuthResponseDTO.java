package com.mediapp.juanb.juanm.mediapp.dtos;

public record AuthResponseDTO(
    String token,
    UserResponseDTO user
) {}
