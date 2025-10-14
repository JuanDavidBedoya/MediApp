package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailResponseDTO;
import com.mediapp.juanb.juanm.mediapp.services.FormulaDetailService;

import jakarta.validation.Valid;

import java.util.List;
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
@RequestMapping("/formula-details")
public class FormulaDetailController {

    private final FormulaDetailService formulaDetailService;

    public FormulaDetailController(FormulaDetailService formulaDetailService) {
        this.formulaDetailService = formulaDetailService;
    }

    @PostMapping
    public ResponseEntity<FormulaDetailResponseDTO> addFormulaDetail(@Valid @RequestBody FormulaDetailRequestDTO requestDTO) {
        FormulaDetailResponseDTO response = formulaDetailService.save(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FormulaDetailResponseDTO>> getAllFormulaDetails() {
        List<FormulaDetailResponseDTO> response = formulaDetailService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormulaDetailResponseDTO> getFormulaDetailById(@PathVariable UUID id) {
        FormulaDetailResponseDTO response = formulaDetailService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormulaDetailResponseDTO> updateFormulaDetail(@PathVariable UUID id, 
                                                                      @Valid @RequestBody FormulaDetailRequestDTO requestDTO) {
        FormulaDetailResponseDTO response = formulaDetailService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormulaDetail(@PathVariable UUID id) {
        formulaDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}