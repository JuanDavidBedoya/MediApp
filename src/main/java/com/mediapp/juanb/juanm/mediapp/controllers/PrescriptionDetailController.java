package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.PrescriptionDetail;
import com.mediapp.juanb.juanm.mediapp.services.PrescriptionDetailService;

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
@RequestMapping("detallerPrescripcion")
public class PrescriptionDetailController {

    private PrescriptionDetailService prescriptionDetailService;

    public PrescriptionDetailController(PrescriptionDetailService prescriptionDetailService){
        this.prescriptionDetailService=prescriptionDetailService;
    }

    @GetMapping
    public ResponseEntity <List<PrescriptionDetail>> getAll() {
        List<PrescriptionDetail> list= prescriptionDetailService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public Optional <PrescriptionDetail> getById(@PathVariable("id") UUID id) {
        return prescriptionDetailService.findById(id);
    }

    @PostMapping
    public ResponseEntity <PrescriptionDetail> save(@RequestBody PrescriptionDetail prescriptionDetail){
        PrescriptionDetail newPrescriptionDetail = prescriptionDetailService.save(prescriptionDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPrescriptionDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity <PrescriptionDetail> update(@PathVariable ("id") UUID id, @RequestBody PrescriptionDetail prescriptionDetail) {
        PrescriptionDetail newPrescriptionDetail = prescriptionDetailService.update(id, prescriptionDetail);
        return ResponseEntity.ok(newPrescriptionDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable("id") UUID id) {
       prescriptionDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
