package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.DoctorAvailability;
import com.mediapp.juanb.juanm.mediapp.services.DoctorAvailabilityService;

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
@RequestMapping("disponibilidadDoctor")
public class DoctorAvailabilityController {

    private DoctorAvailabilityService doctorAvailabilityService;

    public DoctorAvailabilityController(DoctorAvailabilityService doctorAvailabilityService){
        this.doctorAvailabilityService=doctorAvailabilityService;
    }

    @GetMapping
    public ResponseEntity <List<DoctorAvailability>> getAll() {
        List<DoctorAvailability> list= doctorAvailabilityService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public Optional <DoctorAvailability> getById(@PathVariable("id") UUID id) {
        return doctorAvailabilityService.findById(id);
    }

    @PostMapping
    public ResponseEntity <DoctorAvailability> save(@RequestBody DoctorAvailability doctorAvailability){
        DoctorAvailability newDoctorAvailability = doctorAvailabilityService.save(doctorAvailability);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDoctorAvailability);
    }

    @PutMapping("/{id}")
    public ResponseEntity <DoctorAvailability> update(@PathVariable ("id") UUID id, @RequestBody DoctorAvailability doctorAvailability) {
        DoctorAvailability newDoctorAvailability = doctorAvailabilityService.update(id, doctorAvailability);
        return ResponseEntity.ok(newDoctorAvailability);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable("id") UUID id) {
       doctorAvailabilityService.delete(id);
       return ResponseEntity.noContent().build();
    }
}
