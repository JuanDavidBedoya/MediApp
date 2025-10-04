package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Eps;
import com.mediapp.juanb.juanm.mediapp.services.EpsService;

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
@RequestMapping("/eps")
public class EpsController {

    private EpsService epsService;

    public EpsController(EpsService epsService) {
        this.epsService = epsService;
    }

    @GetMapping
    public ResponseEntity<List<Eps>> getAll(){
        List<Eps> list = epsService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Eps> getById(@PathVariable("uuid") UUID uuid){
        return epsService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Eps> save(@RequestBody Eps eps) {
        Eps newEps = epsService.save(eps);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEps);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Eps> update(@PathVariable("uuid") UUID uuid, @RequestBody Eps eps) {
        Eps updatedEps = epsService.update(uuid, eps);
        return ResponseEntity.ok(updatedEps);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        epsService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}