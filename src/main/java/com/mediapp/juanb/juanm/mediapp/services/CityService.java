package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.City;
import com.mediapp.juanb.juanm.mediapp.repositories.CityRepository;

@Service
public class CityService {

    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public Optional<City> findById(UUID uuid) {
        return cityRepository.findById(uuid);
    }

    public City save(City city) {
        return cityRepository.save(city);
    }

    public void delete(UUID uuid) {
        cityRepository.deleteById(uuid);
    }

    public City update(UUID uuid, City city) {
        city.setIdCity(uuid);
        return cityRepository.save(city);
    }
}