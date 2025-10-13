package com.mediapp.juanb.juanm.mediapp.controllers;

import com.mediapp.juanb.juanm.mediapp.dtos.AuthResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.LoginRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import com.mediapp.juanb.juanm.mediapp.services.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO newUser = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}