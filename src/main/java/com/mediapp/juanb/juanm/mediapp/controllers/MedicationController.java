package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Medication;
import com.mediapp.juanb.juanm.mediapp.services.MedicationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/medicamentos")
public class MedicationController {

    private MedicationService medicationService;

    public MedicationController(MedicationService medicationService){
        this.medicationService=medicationService;
    }

    @GetMapping
    public ResponseEntity <List<Medication>> getAll() {
        List<Medication> list= medicationService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional <Medication> getById(@PathVariable("id") UUID id) {
        return medicationService.findById(id);
    }

    @PostMapping
    public ResponseEntity <Medication> save(@RequestBody Medication medication){
        Medication newMedication = medicationService.save(medication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMedication);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity <Medication> update(@PathVariable ("id") UUID id, @RequestBody Medication medication) {
        Medication newMedication = medicationService.update(id, medication);
        return ResponseEntity.ok(newMedication);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity <Void> delete(@PathVariable("id") UUID id) {
       medicationService.delete(id);
       return ResponseEntity.noContent().build();
    }
    

}
