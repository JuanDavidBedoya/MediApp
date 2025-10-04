package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.services.FormulaDetailService;

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
@RequestMapping("/formula_details")
public class FormulaDetailController {

    private FormulaDetailService formulaDetailService;

    public FormulaDetailController(FormulaDetailService formulaDetailService) {
        this.formulaDetailService = formulaDetailService;
    }

    @GetMapping
    public ResponseEntity<List<FormulaDetail>> getAll(){
        List<FormulaDetail> list = formulaDetailService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<FormulaDetail> getById(@PathVariable("uuid") UUID uuid){
        return formulaDetailService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<FormulaDetail> save(@RequestBody FormulaDetail formulaDetail) {
        FormulaDetail newFormulaDetail = formulaDetailService.save(formulaDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFormulaDetail);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<FormulaDetail> update(@PathVariable("uuid") UUID uuid, @RequestBody FormulaDetail formulaDetail) {
        FormulaDetail updatedFormulaDetail = formulaDetailService.update(uuid, formulaDetail);
        return ResponseEntity.ok(updatedFormulaDetail);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        formulaDetailService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}