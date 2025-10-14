package com.mediapp.juanb.juanm.mediapp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mediapp.juanb.juanm.mediapp.dtos.UserRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserUpdateDTO;
import com.mediapp.juanb.juanm.mediapp.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<UserResponseDTO> getByCedula(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(userService.findByCedula(cedula));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO userDTO) {
        UserResponseDTO newUser = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable("cedula") String cedula, @Valid @RequestBody UserUpdateDTO userDTO) {
        UserResponseDTO updatedUser = userService.update(cedula, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        userService.delete(cedula);
        return ResponseEntity.noContent().build();
    }
}