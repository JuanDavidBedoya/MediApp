package com.mediapp.juanb.juanm.mediapp.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Patient;
import com.mediapp.juanb.juanm.mediapp.services.PatientService;

@RestController
@RequestMapping("/pacientes")
public class PatientController {
    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAll(){
        List<Patient> list = patientService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{cedula}")
    public Optional<Patient> getByCedula(@PathVariable("cedula") String cedula){
        return patientService.findById(cedula);
    }
    
    @PostMapping
    public ResponseEntity<Patient> save(@RequestBody Patient patient) {
        Patient newPatient = patientService.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPatient);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<Patient> update(@PathVariable("cedula") String cedula, @RequestBody Patient patient) {
        Patient updatedPatient = patientService.update(cedula, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        patientService.delete(cedula);
        return ResponseEntity.noContent().build();
    }
}
