package com.mediapp.juanb.juanm.mediapp.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.entities.DoctorSpeciality;
import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.services.DoctorSpecialityService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/doctor-especialidades")
public class DoctorSpecialityController {

    private DoctorSpecialityService doctorSpecialityService;

    public DoctorSpecialityController(DoctorSpecialityService doctorSpecialityService) {
        this.doctorSpecialityService = doctorSpecialityService;
    }

    @GetMapping
    public List<DoctorSpeciality> getAll() {
        return doctorSpecialityService.findAll();
    }

    @PostMapping
    public DoctorSpeciality create(@RequestBody DoctorSpeciality doctorSpeciality) {
        return doctorSpecialityService.save(doctorSpeciality);
    }

    @DeleteMapping("/by-doctor/{doctorId}")
    public ResponseEntity<Void> deleteByDoctorId(@PathVariable String doctorId) {
        doctorSpecialityService.deleteByDoctorId(doctorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-speciality/{specialityId}")
    public ResponseEntity<Void> deleteBySpecialityId(@PathVariable UUID specialityId) {
        doctorSpecialityService.deleteBySpecialityId(specialityId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/doctor")
    public DoctorSpeciality updateDoctor(@PathVariable String id, @RequestBody Doctor doctor) {
        return doctorSpecialityService.updateDoctor(id, doctor);
    }

    @PutMapping("/{id}/speciality")
    public DoctorSpeciality updateSpeciality(@PathVariable String id, @RequestBody Speciality speciality) {
        return doctorSpecialityService.updateSpeciality(id, speciality);
    }
}
