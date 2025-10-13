package com.mediapp.juanb.juanm.mediapp.mappers;

import com.mediapp.juanb.juanm.mediapp.dtos.UserPhoneResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.UserPhone;
import org.springframework.stereotype.Component;

@Component
public class UserPhoneMapper {

    public UserPhoneResponseDTO toResponseDTO(UserPhone userPhone) {
        if (userPhone == null) {
            return null;
        }

        String userCedula = (userPhone.getUser() != null) ? userPhone.getUser().getCedula() : null;
        String userName = (userPhone.getUser() != null) ? userPhone.getUser().getName() : null;
        String phoneNumber = (userPhone.getPhone() != null) ? userPhone.getPhone().getPhone() : null;

        return new UserPhoneResponseDTO(
            userPhone.getIdUserPhone(),
            userCedula,
            userName,
            phoneNumber
        );
    }
}
