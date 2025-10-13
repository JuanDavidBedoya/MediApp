package com.mediapp.juanb.juanm.mediapp.dtos;

public record DoctorResponseDTO(
    String cedula,
    String name,
    String phone,
    String email,
    String specialityName
) {}