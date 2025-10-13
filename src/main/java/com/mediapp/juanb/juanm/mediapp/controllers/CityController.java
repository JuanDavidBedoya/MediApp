package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.services.CityService;

import java.util.List;
import java.util.UUID;

import com.mediapp.juanb.juanm.mediapp.dtos.CityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.CityResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityResponseDTO>> getAll() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CityResponseDTO> getById(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(cityService.findById(uuid));
    }

    @PostMapping
    public ResponseEntity<CityResponseDTO> save(@Valid @RequestBody CityRequestDTO cityDTO) {
        CityResponseDTO newCity = cityService.save(cityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCity);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<CityResponseDTO> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody CityRequestDTO cityDTO) {
        CityResponseDTO updatedCity = cityService.update(uuid, cityDTO);
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        cityService.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}