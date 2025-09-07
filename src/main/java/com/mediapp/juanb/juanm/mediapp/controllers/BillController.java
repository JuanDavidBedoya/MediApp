package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.Bill;
import com.mediapp.juanb.juanm.mediapp.services.BillService;

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
@RequestMapping("facturas")
public class BillController {

    private BillService billService;

    public BillController(BillService billService){
        this.billService=billService;
    }

    @GetMapping
    public ResponseEntity <List<Bill>> getAll() {
        List<Bill> list= billService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public Optional <Bill> getById(@PathVariable("id") UUID id) {
        return billService.findById(id);
    }

    @PostMapping
    public ResponseEntity <Bill> save(@RequestBody Bill bill){
        Bill newBill = billService.save(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBill);
    }

    @PutMapping("/{id}")
    public ResponseEntity <Bill> update(@PathVariable ("id") UUID id, @RequestBody Bill bill) {
        Bill newBill = billService.update(id, bill);
        return ResponseEntity.ok(newBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable("id") UUID id) {
       billService.delete(id);
        return ResponseEntity.noContent().build();
    }
    

}
