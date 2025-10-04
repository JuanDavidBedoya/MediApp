package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.repositories.PhoneRepository;

@Service
public class PhoneService {

    private PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public List<Phone> findAll() {
        return phoneRepository.findAll();
    }

    public Optional<Phone> findById(UUID uuid) {
        return phoneRepository.findById(uuid);
    }

    public Phone save(Phone phone) {
        return phoneRepository.save(phone);
    }

    public void delete(UUID uuid) {
        phoneRepository.deleteById(uuid);
    }

    public Phone update(UUID uuid, Phone phone) {
        phone.setIdPhone(uuid);
        return phoneRepository.save(phone);
    }
}