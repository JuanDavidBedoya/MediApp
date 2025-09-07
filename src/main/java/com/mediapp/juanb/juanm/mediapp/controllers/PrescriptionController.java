package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Prescription;
import com.mediapp.juanb.juanm.mediapp.services.PrescriptionService;

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
@RequestMapping("prescripciones")
public class PrescriptionController {

    private PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService){
        this.prescriptionService=prescriptionService;
    }

    @GetMapping
    public ResponseEntity <List<Prescription>> getAll() {
        List<Prescription> list= prescriptionService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public Optional <Prescription> getById(@PathVariable("id") UUID id) {
        return prescriptionService.findById(id);
    }

    @PostMapping
    public ResponseEntity <Prescription> save(@RequestBody Prescription prescription){
        Prescription newPrescription = prescriptionService.save(prescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPrescription);
    }

    @PutMapping("/{id}")
    public ResponseEntity <Prescription> update(@PathVariable ("id") UUID id, @RequestBody Prescription prescription) {
        Prescription newPrescription = prescriptionService.update(id, prescription);
        return ResponseEntity.ok(newPrescription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable("id") UUID id) {
       prescriptionService.delete(id);
       return ResponseEntity.noContent().build();
    }
    
    
}
