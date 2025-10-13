package com.mediapp.juanb.juanm.mediapp.controllers;

import com.mediapp.juanb.juanm.mediapp.dtos.PhoneRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.PhoneResponseDTO;
import com.mediapp.juanb.juanm.mediapp.services.PhoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/phones")
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    public ResponseEntity<List<PhoneResponseDTO>> getAll() {
        return ResponseEntity.ok(phoneService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PhoneResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(phoneService.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<PhoneResponseDTO> save(@Valid @RequestBody PhoneRequestDTO phoneDTO) {
        PhoneResponseDTO newPhone = phoneService.save(phoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPhone);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PhoneResponseDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody PhoneRequestDTO phoneDTO) {
        PhoneResponseDTO updatedPhone = phoneService.update(uuid, phoneDTO);
        return ResponseEntity.ok(updatedPhone);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        phoneService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}