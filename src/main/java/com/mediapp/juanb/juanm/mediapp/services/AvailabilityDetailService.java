package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.AvailabilityDetail;
import com.mediapp.juanb.juanm.mediapp.repositories.AvailabilityDetailRepository;

@Service
public class AvailabilityDetailService {

    private AvailabilityDetailRepository availabilityDetailRepository;

    public AvailabilityDetailService(AvailabilityDetailRepository availabilityDetailRepository) {
        this.availabilityDetailRepository = availabilityDetailRepository;
    }

    public List<AvailabilityDetail> findAll() {
        return availabilityDetailRepository.findAll();
    }
    
    public Optional<AvailabilityDetail> findById(UUID uuid) {
        return availabilityDetailRepository.findById(uuid);
    }

    public AvailabilityDetail save(AvailabilityDetail availabilityDetail) {
        return availabilityDetailRepository.save(availabilityDetail);
    }

    public void delete(UUID uuid) {
        availabilityDetailRepository.deleteById(uuid);
    }

    public AvailabilityDetail update(UUID uuid, AvailabilityDetail availabilityDetail) {
        availabilityDetail.setUuidAvailabilityDetail(uuid);
        return availabilityDetailRepository.save(availabilityDetail);
    }
}
