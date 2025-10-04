package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.UserPhone;
import com.mediapp.juanb.juanm.mediapp.services.UserPhoneService;

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
@RequestMapping("/user_phones")
public class UserPhoneController {

    private UserPhoneService userPhoneService;

    public UserPhoneController(UserPhoneService userPhoneService) {
        this.userPhoneService = userPhoneService;
    }

    @GetMapping
    public ResponseEntity<List<UserPhone>> getAll(){
        List<UserPhone> list = userPhoneService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<UserPhone> getById(@PathVariable("uuid") UUID uuid){
        return userPhoneService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<UserPhone> save(@RequestBody UserPhone userPhone) {
        UserPhone newUserPhone = userPhoneService.save(userPhone);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserPhone);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserPhone> update(@PathVariable("uuid") UUID uuid, @RequestBody UserPhone userPhone) {
        UserPhone updatedUserPhone = userPhoneService.update(uuid, userPhone);
        return ResponseEntity.ok(updatedUserPhone);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        userPhoneService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}