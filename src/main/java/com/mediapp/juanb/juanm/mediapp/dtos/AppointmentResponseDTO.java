package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.Date;
import java.util.UUID;

public record AppointmentResponseDTO(
    UUID idAppointment,
    String doctorCedula,
    String patientCedula,
    Date date,
    Date time,
    String observations
) {}