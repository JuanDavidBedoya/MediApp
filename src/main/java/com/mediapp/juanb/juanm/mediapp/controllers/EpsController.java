package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.dtos.EpsRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.EpsResponseDTO;
import com.mediapp.juanb.juanm.mediapp.services.EpsService;

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
@RequestMapping("/eps")
public class EpsController {

    private final EpsService epsService;

    public EpsController(EpsService epsService) {
        this.epsService = epsService;
    }

    @GetMapping
    public ResponseEntity<List<EpsResponseDTO>> getAll() {
        return ResponseEntity.ok(epsService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EpsResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(epsService.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<EpsResponseDTO> save(@Valid @RequestBody EpsRequestDTO epsDTO) {
        EpsResponseDTO newEps = epsService.save(epsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEps);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<EpsResponseDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody EpsRequestDTO epsDTO) {
        EpsResponseDTO updatedEps = epsService.update(uuid, epsDTO);
        return ResponseEntity.ok(updatedEps);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        epsService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}