package com.mediapp.juanb.juanm.mediapp.mappers;

import com.mediapp.juanb.juanm.mediapp.entities.User;

import io.jsonwebtoken.lang.Collections;

import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        String epsName = (user.getEps() != null) ? user.getEps().getName() : null;
        String cityName = (user.getCity() != null) ? user.getCity().getName() : null;


        // Extraer los números de teléfono de la relación UserPhone
        List<String> phoneNumbers = user.getUserPhones() != null ?
            user.getUserPhones().stream()
                .map(userPhone -> userPhone.getPhone().getPhone())
                .collect(Collectors.toList()) :
            Collections.emptyList();

        return new UserResponseDTO(
                user.getCedula(),
                user.getName(),
                user.getEmail(),
                epsName, 
                phoneNumbers,
                cityName
        );
    }
}
