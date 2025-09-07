package com.mediapp.juanb.juanm.mediapp.controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.services.SpecialityService;

@RestController
@RequestMapping("/especialidades")
public class SpecialityController {

    private SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping
    public ResponseEntity<List<Speciality>> getAll(){
        List<Speciality> list = specialityService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Speciality> getById(@PathVariable("cedula") UUID uuid){
        return specialityService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Speciality> save(@RequestBody Speciality speciality) {
        Speciality newSpeciality = specialityService.save(speciality);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpeciality);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Speciality> update(@PathVariable("uuid") UUID uuid, @RequestBody Speciality speciality) {
        Speciality updatedSpeciality = specialityService.update(uuid, speciality);
        return ResponseEntity.ok(updatedSpeciality);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        specialityService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}
