package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

public record FormulaDetailResponseDTO(
    UUID idFormulaDetail,
    UUID formulaId,
    String appointmentId,
    String name,
    int quantity,
    String dosage
) {}