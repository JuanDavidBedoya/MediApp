package com.mediapp.juanb.juanm.mediapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediapp.juanb.juanm.mediapp.entities.City;
import com.mediapp.juanb.juanm.mediapp.services.CityService;

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
@RequestMapping("/cities")
public class CityController {

    private CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAll(){
        List<City> list = cityService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{uuid}")
    public Optional<City> getById(@PathVariable("uuid") UUID uuid){
        return cityService.findById(uuid);
    }
    
    @PostMapping
    public ResponseEntity<City> save(@RequestBody City city) {
        City newCity = cityService.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCity);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<City> update(@PathVariable("uuid") UUID uuid, @RequestBody City city) {
        City updatedCity = cityService.update(uuid, city);
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        cityService.delete(uuid);
        return ResponseEntity.noContent().build();
    }   
}