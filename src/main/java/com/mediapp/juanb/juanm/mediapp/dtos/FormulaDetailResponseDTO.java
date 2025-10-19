package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

public record FormulaDetailResponseDTO(
    UUID idFormulaDetail,
    UUID formulaId,
    String name,
    int quantity,
    String dosage
) {}