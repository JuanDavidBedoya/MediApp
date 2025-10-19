package com.mediapp.juanb.juanm.mediapp.controllers;

import com.mediapp.juanb.juanm.mediapp.dtos.DoctorResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.DoctorUpdateDTO;
import com.mediapp.juanb.juanm.mediapp.services.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAll() {
        return ResponseEntity.ok(doctorService.findAll());
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<DoctorResponseDTO> getById(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(doctorService.findById(cedula));
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<DoctorResponseDTO> update(@PathVariable("cedula") String cedula, @Valid @RequestBody DoctorUpdateDTO doctorDTO) {
        DoctorResponseDTO updatedDoctor = doctorService.update(cedula, doctorDTO);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        doctorService.delete(cedula);
        return ResponseEntity.noContent().build();
    }
}