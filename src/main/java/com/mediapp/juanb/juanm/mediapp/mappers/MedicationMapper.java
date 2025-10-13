package com.mediapp.juanb.juanm.mediapp.mappers;

import org.springframework.stereotype.Component;

import com.mediapp.juanb.juanm.mediapp.dtos.MedicationRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.MedicationResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Medication;

@Component
public class MedicationMapper {
    
    public static Medication toEntity(MedicationRequestDTO dto) {
        if (dto == null) return null;
        Medication entity = new Medication();
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        return entity;
    }

    public static MedicationResponseDTO toResponseDTO(Medication entity) {
        if (entity == null) return null;
        return new MedicationResponseDTO(
            entity.getIdMedication(),
            entity.getName(),
            entity.getPrice()
        );
    }
}
