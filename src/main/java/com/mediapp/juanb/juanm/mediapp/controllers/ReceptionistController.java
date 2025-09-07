package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Receptionist;
import com.mediapp.juanb.juanm.mediapp.services.ReceptionistService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/recepcionistas")
public class ReceptionistController {

    private ReceptionistService receptionistService;

    public ReceptionistController(ReceptionistService receptionistService){
        this.receptionistService=receptionistService;
    }

    @GetMapping
    public ResponseEntity <List<Receptionist>> getAll() {
        List<Receptionist> list= receptionistService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{cedula}")
    public Optional <Receptionist> getById(@PathVariable("cedula") String cedula) {
        return receptionistService.findById(cedula);
    }

    @PostMapping
    public ResponseEntity <Receptionist> save(@RequestBody Receptionist receptionist){
        Receptionist newReceptionist = receptionistService.save(receptionist);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReceptionist);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity <Receptionist> update(@PathVariable ("cedula") String cedula, @RequestBody Receptionist receptionist) {
        Receptionist newReceptionist = receptionistService.update(cedula, receptionist);
        return ResponseEntity.ok(newReceptionist);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity <Void> delete(@PathVariable("id") String cedula) {
       receptionistService.delete(cedula);
       return ResponseEntity.noContent().build();
    }
    
}
