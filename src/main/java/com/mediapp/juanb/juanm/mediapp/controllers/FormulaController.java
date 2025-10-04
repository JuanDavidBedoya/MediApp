package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.services.FormulaService;

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
@RequestMapping("/formulas")
public class FormulaController {

    private FormulaService formulaService;

    public FormulaController(FormulaService formulaService) {
        this.formulaService = formulaService;
    }

    @GetMapping
    public ResponseEntity<List<Formula>> getAll(){
        List<Formula> list = formulaService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<Formula> getById(@PathVariable("uuid") UUID uuid){
        return formulaService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<Formula> save(@RequestBody Formula formula) {
        Formula newFormula = formulaService.save(formula);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFormula);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Formula> update(@PathVariable("uuid") UUID uuid, @RequestBody Formula formula) {
        Formula updatedFormula = formulaService.update(uuid, formula);
        return ResponseEntity.ok(updatedFormula);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        formulaService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}