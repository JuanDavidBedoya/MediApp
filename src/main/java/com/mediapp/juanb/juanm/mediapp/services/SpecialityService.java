package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.repositories.SpecialityRepository;

@Service
public class SpecialityService {

    private SpecialityRepository specialityRepository;
    
    public SpecialityService(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    public List<Speciality> findAll() {
        return specialityRepository.findAll();
    }

    public Optional<Speciality> findById(UUID uuid) {
        return specialityRepository.findById(uuid);
    }

    public Speciality save(Speciality speciality) {
        return specialityRepository.save(speciality);
    }

    public void delete(UUID uuid) {
        specialityRepository.deleteById(uuid);
    }

    public Speciality update(UUID uuid, Speciality speciality) {
        speciality.setId(uuid);
        return specialityRepository.save(speciality);
    }
}
