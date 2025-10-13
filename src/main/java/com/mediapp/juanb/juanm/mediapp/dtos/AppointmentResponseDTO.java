package com.mediapp.juanb.juanm.mediapp.dtos;

import java.sql.Time;
import java.sql.Date;
import java.util.UUID;

public record AppointmentResponseDTO(
    UUID idAppointment,
    String doctorCedula,
    String patientCedula,
    Date date,
    Time time,
    String observations
) {}