package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.services.PhoneService;

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
@RequestMapping("/phones")
public class PhoneController {

    private PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    public ResponseEntity<List<Phone>> getAll(){
        List<Phone> list = phoneService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Phone> getById(@PathVariable("uuid") UUID uuid){
        return phoneService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Phone> save(@RequestBody Phone phone) {
        Phone newPhone = phoneService.save(phone);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPhone);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Phone> update(@PathVariable("uuid") UUID uuid, @RequestBody Phone phone) {
        Phone updatedPhone = phoneService.update(uuid, phone);
        return ResponseEntity.ok(updatedPhone);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        phoneService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}