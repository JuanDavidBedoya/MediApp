package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.services.DoctorService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAll(){
        List<Doctor> list = doctorService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{cedula}")
    public Optional<Doctor> getById(@PathVariable("cedula") String cedula){
        return doctorService.findById(cedula);
    }
    
    @PostMapping
    public ResponseEntity<Doctor> save(@RequestBody Doctor doctor) {
        Doctor newDoctor = doctorService.save(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDoctor);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<Doctor> update(@PathVariable("cedula") String cedula, @RequestBody Doctor doctor) {
        Doctor updatedDoctor = doctorService.update(cedula, doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        doctorService.delete(cedula);
        return ResponseEntity.noContent().build();
    }   
}