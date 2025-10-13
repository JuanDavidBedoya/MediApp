package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
    @NotBlank(message = "La cédula no puede estar vacía")
    @Size(min = 5, max = 15, message = "La cédula debe tener entre 5 y 15 caracteres")
    String cedula,

    @NotBlank(message = "El nombre no puede estar vacío")
    String name,

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    String email,

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String contrasena,

    @NotBlank(message = "El nombre de la EPS es obligatorio")
    String epsName, 

    List<String> phones,

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    String cityName
) {}