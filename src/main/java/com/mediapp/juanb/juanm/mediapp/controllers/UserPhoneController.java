package com.mediapp.juanb.juanm.mediapp.controllers;

import com.mediapp.juanb.juanm.mediapp.dtos.UserPhoneRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserPhoneResponseDTO;
import com.mediapp.juanb.juanm.mediapp.services.UserPhoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-phones") 
public class UserPhoneController {

    private final UserPhoneService userPhoneService;

    public UserPhoneController(UserPhoneService userPhoneService) {
        this.userPhoneService = userPhoneService;
    }

    @GetMapping
    public ResponseEntity<List<UserPhoneResponseDTO>> getAll() {
        return ResponseEntity.ok(userPhoneService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserPhoneResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(userPhoneService.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<UserPhoneResponseDTO> save(@Valid @RequestBody UserPhoneRequestDTO requestDTO) {
        UserPhoneResponseDTO newUserPhone = userPhoneService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserPhone);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserPhoneResponseDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody UserPhoneRequestDTO requestDTO) {
        UserPhoneResponseDTO updatedUserPhone = userPhoneService.update(uuid, requestDTO);
        return ResponseEntity.ok(updatedUserPhone);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        userPhoneService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}