package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.History;
import com.mediapp.juanb.juanm.mediapp.services.HistoryService;

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
@RequestMapping("/historiales")
public class HistoryController {

    private HistoryService historyService;

    public HistoryController(HistoryService historyService){
        this.historyService=historyService;
    }

    @GetMapping
    public ResponseEntity <List<History>> getAll() {
        List<History> list= historyService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public Optional <History> getById(@PathVariable("id") UUID id) {
        return historyService.findById(id);
    }

    @PostMapping
    public ResponseEntity <History> save(@RequestBody History history){
        History newHistory = historyService.save(history);
        return ResponseEntity.status(HttpStatus.CREATED).body(newHistory);
    }

    @PutMapping("/{id}")
    public ResponseEntity <History> update(@PathVariable ("id") UUID id, @RequestBody History history) {
        History newHistory = historyService.update(id, history);
        return ResponseEntity.ok(newHistory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable("id") UUID id) {
       historyService.delete(id);
       return ResponseEntity.noContent().build();
    }
    

}
