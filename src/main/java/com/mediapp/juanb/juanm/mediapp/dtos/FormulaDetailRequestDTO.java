package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

public record FormulaDetailRequestDTO(
    UUID formulaId,
    UUID medicationId,
    int quantity,
    String dosage
) {}
