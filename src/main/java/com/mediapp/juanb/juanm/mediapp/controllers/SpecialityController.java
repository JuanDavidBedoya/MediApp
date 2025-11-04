package com.mediapp.juanb.juanm.mediapp.controllers;

import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.services.SpecialityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specialities")
public class SpecialityController {

    private final SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping
    public ResponseEntity<List<SpecialityResponseDTO>> getAll() {
        return ResponseEntity.ok(specialityService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<SpecialityResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(specialityService.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<SpecialityResponseDTO> save(@Valid @RequestBody SpecialityRequestDTO specialityDTO) {
        SpecialityResponseDTO newSpeciality = specialityService.save(specialityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpeciality);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<SpecialityResponseDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody SpecialityRequestDTO specialityDTO) {
        SpecialityResponseDTO updatedSpeciality = specialityService.update(uuid, specialityDTO);
        return ResponseEntity.ok(updatedSpeciality);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        specialityService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}