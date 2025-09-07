package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.DoctorAvailability;
import com.mediapp.juanb.juanm.mediapp.repositories.DoctorAvailabilityRepository;

@Service
public class DoctorAvailabilityService {

    private DoctorAvailabilityRepository doctorAvailabilityRepository;
    
    public DoctorAvailabilityService(DoctorAvailabilityRepository doctorAvailabilityRepository) {
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
    }

    public List<DoctorAvailability> findAll() {
        return doctorAvailabilityRepository.findAll();
    }

    public Optional<DoctorAvailability> findById(UUID uuid) {
        return doctorAvailabilityRepository.findById(uuid);
    }

    public DoctorAvailability save(DoctorAvailability doctorAvailability) {
        return doctorAvailabilityRepository.save(doctorAvailability);
    }

    public void delete(UUID uuid) {
        doctorAvailabilityRepository.deleteById(uuid);
    }

    public DoctorAvailability update(UUID uuid, DoctorAvailability doctorAvailability) {
        doctorAvailability.setUuid(uuid);
        return doctorAvailabilityRepository.save(doctorAvailability);
    }
}
