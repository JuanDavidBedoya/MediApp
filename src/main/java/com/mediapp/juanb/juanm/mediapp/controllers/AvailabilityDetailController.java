package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.AvailabilityDetail;
import com.mediapp.juanb.juanm.mediapp.services.AvailabilityDetailService;

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
@RequestMapping("/detallesDiponibilidad")
public class AvailabilityDetailController {

    private AvailabilityDetailService availabilityDetailService;

    public AvailabilityDetailController(AvailabilityDetailService availabilityDetailService){
        this.availabilityDetailService=availabilityDetailService;
    }

    @GetMapping
    public ResponseEntity <List<AvailabilityDetail>> getAll() {
        List<AvailabilityDetail> list= availabilityDetailService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public Optional <AvailabilityDetail> getById(@PathVariable("id") UUID id) {
        return availabilityDetailService.findById(id);
    }

    @PostMapping
    public ResponseEntity <AvailabilityDetail> save(@RequestBody AvailabilityDetail availabilityDetail){
        AvailabilityDetail newAvailabilityDetail = availabilityDetailService.save(availabilityDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAvailabilityDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity <AvailabilityDetail> update(@PathVariable ("id") UUID id, @RequestBody AvailabilityDetail availabilityDetail) {
        AvailabilityDetail newAvailabilityDetail = availabilityDetailService.update(id, availabilityDetail);
        return ResponseEntity.ok(newAvailabilityDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable("id") UUID id) {
       availabilityDetailService.delete(id);
       return ResponseEntity.noContent().build();
    }
    

}
