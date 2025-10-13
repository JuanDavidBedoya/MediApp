package com.mediapp.juanb.juanm.mediapp.mappers;

import com.mediapp.juanb.juanm.mediapp.dtos.PhoneRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.PhoneResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper {

    public PhoneResponseDTO toResponseDTO(Phone phone) {
        if (phone == null) {
            return null;
        }
        return new PhoneResponseDTO(
            phone.getIdPhone(),
            phone.getPhone()
        );
    }

    public Phone toEntity(PhoneRequestDTO phoneDTO) {
        if (phoneDTO == null) {
            return null;
        }
        Phone phone = new Phone();
        phone.setPhone(phoneDTO.phone());
        return phone;
    }
}
