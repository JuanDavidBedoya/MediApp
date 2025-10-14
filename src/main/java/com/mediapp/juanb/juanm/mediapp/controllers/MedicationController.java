package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.dtos.MedicationRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.MedicationResponseDTO;
import com.mediapp.juanb.juanm.mediapp.services.MedicationService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private MedicationService medicationService;

    public MedicationController(MedicationService medicationService){
        this.medicationService=medicationService;
    }

    @PostMapping
    public ResponseEntity<MedicationResponseDTO> createMedication(@Valid @RequestBody MedicationRequestDTO requestDTO) {
        MedicationResponseDTO response = medicationService.save(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponseDTO>> getAllMedications() {
        List<MedicationResponseDTO> response = medicationService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDTO> getMedicationById(@PathVariable UUID id) {
        MedicationResponseDTO response = medicationService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponseDTO> updateMedication(@PathVariable UUID id, 
                                                                @Valid @RequestBody MedicationRequestDTO requestDTO) {
        MedicationResponseDTO response = medicationService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable UUID id) {
        medicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}