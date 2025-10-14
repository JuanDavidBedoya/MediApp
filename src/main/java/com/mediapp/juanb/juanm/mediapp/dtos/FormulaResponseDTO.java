package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record FormulaResponseDTO(
    UUID idFormula,
    UUID appointmentId,
    Date date,
    List<FormulaDetailResponseDTO> medications
) {}