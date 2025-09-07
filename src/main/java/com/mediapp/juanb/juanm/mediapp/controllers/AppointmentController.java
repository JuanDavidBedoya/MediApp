package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.services.AppointmentService;

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

@RestController
@RequestMapping("/citas")
public class AppointmentController {

    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAll(){
        List<Appointment> list = appointmentService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Appointment> getById(@PathVariable("uuid") UUID uuid){
        return appointmentService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Appointment> save(@RequestBody Appointment appointment) {
        Appointment newAppointment = appointmentService.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Appointment> update(@PathVariable("uuid") UUID uuid, @RequestBody Appointment appointment) {
        Appointment updatedAppointment = appointmentService.update(uuid, appointment);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        appointmentService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
    
    
}
