package com.mediapp.juanb.juanm.mediapp.dtos;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
    @NotBlank(message = "El nombre no puede estar vacío")
    String name,

    @NotBlank(message = "El correo no puede estar vacío")
    @Email
    String email,

    // La contraseña es opcional al actualizar. Si viene nula o vacía, no se cambia.
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    String contrasena,

    @NotBlank(message = "El nombre de la EPS es obligatorio")
    String epsName,

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    String cityName,

    // La lista de teléfonos puede ser nula o vacía si no se desea actualizar.
    List<String> phones
) {}
