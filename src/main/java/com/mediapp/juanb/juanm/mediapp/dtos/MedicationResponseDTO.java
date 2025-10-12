package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

public record MedicationResponseDTO(
    UUID idMedication,
    String name,
    Float price
) {}
