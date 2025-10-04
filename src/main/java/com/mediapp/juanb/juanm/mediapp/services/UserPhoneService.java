package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.UserPhone;
import com.mediapp.juanb.juanm.mediapp.repositories.UserPhoneRepository;

@Service
public class UserPhoneService {

    private UserPhoneRepository userPhoneRepository;

    public UserPhoneService(UserPhoneRepository userPhoneRepository) {
        this.userPhoneRepository = userPhoneRepository;
    }

    public List<UserPhone> findAll() {
        return userPhoneRepository.findAll();
    }

    public Optional<UserPhone> findById(UUID uuid) {
        return userPhoneRepository.findById(uuid);
    }

    public UserPhone save(UserPhone userPhone) {
        return userPhoneRepository.save(userPhone);
    }

    public void delete(UUID uuid) {
        userPhoneRepository.deleteById(uuid);
    }

    public UserPhone update(UUID uuid, UserPhone userPhone) {
        userPhone.setIdUserPhone(uuid);
        return userPhoneRepository.save(userPhone);
    }
}