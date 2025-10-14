package com.mediapp.juanb.juanm.mediapp.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DoctorUpdateDTO(

    @NotBlank(message = "El nombre no puede estar vacío")
    String name,

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(min = 7, max = 15)
    String phone,

    @NotBlank(message = "El correo no puede estar vacío")
    @Email
    String email,

    @NotBlank(message = "El nombre de la especialidad es obligatorio")
    String specialityName,

    @NotBlank(message = "La contraseña no puede estar vacía")
    String password
) {}