package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String cedula) {
        return userRepository.findById(cedula);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(String cedula) {
        userRepository.deleteById(cedula);
    }

    public User update(String cedula, User user) {
        user.setId(cedula);
        return userRepository.save(user);
    }
}
