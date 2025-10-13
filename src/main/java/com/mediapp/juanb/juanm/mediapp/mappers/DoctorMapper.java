package com.mediapp.juanb.juanm.mediapp.mappers;

import com.mediapp.juanb.juanm.mediapp.dtos.DoctorResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorResponseDTO toResponseDTO(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        
        String specialityName = (doctor.getSpeciality() != null) ? doctor.getSpeciality().getName() : null;

        return new DoctorResponseDTO(
            doctor.getCedulaDoctor(),
            doctor.getName(),
            doctor.getPhone(),
            doctor.getEmail(),
            specialityName
        );
    }
}