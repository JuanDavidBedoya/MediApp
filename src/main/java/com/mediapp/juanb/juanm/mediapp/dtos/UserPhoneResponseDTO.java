package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.UUID;

public record UserPhoneResponseDTO(
    UUID idUserPhone,
    String userCedula,
    String userName,
    String phoneNumber
) {}