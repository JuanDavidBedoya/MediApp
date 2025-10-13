package com.mediapp.juanb.juanm.mediapp.dtos;

import java.sql.Time;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public record AppointmentRequestDTO(
    @NotBlank(message = "La cédula del doctor es obligatoria")
    String doctorCedula, 
    
    @NotBlank(message = "La cédula del paciente es obligatoria")
    String patientCedula, 
    
    @NotNull(message = "La fecha de la cita es obligatoria")
    Date date,
    
    @NotNull(message = "La hora de la cita es obligatoria")
    Time time,
    
    String observations
) {}
