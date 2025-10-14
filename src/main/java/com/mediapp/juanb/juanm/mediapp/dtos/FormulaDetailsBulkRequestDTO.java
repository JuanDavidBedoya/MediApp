package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record FormulaDetailsBulkRequestDTO(
    @NotNull(message = "El ID de la fórmula es obligatorio")
    UUID formulaId,
    
    @NotEmpty(message = "Debe incluir al menos un medicamento")
    List<FormulaMedicationDTO> medications
) {}
