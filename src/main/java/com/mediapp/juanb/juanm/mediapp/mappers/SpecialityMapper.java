package com.mediapp.juanb.juanm.mediapp.mappers;

import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import org.springframework.stereotype.Component;

@Component
public class SpecialityMapper {

    public SpecialityResponseDTO toResponseDTO(Speciality speciality) {
        if (speciality == null) {
            return null;
        }
        return new SpecialityResponseDTO(
            speciality.getIdSpeciality(),
            speciality.getName()
        );
    }

    public Speciality toEntity(SpecialityRequestDTO specialityDTO) {
        if (specialityDTO == null) {
            return null;
        }
        Speciality speciality = new Speciality();
        speciality.setName(specialityDTO.name());
        return speciality;
    }
}
