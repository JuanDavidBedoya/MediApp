package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.Date;

public record AppointmentRequestDTO(
    String doctorCedula, 
    String patientCedula, 
    Date date,
    Date time,
    String observations
) {}
