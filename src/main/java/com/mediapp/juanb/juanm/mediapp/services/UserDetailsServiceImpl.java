package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.repositories.DoctorRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.UserRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  
        var user = userRepository.findByCedula(username);
        if (user.isPresent()) {
            return user.get();
        }

        var doctor = doctorRepository.findByCedula(username);
        if (doctor.isPresent()) {
            return doctor.get();
        }

        throw new UsernameNotFoundException("No se encontró un usuario o doctor con la cédula: " + username);
    }
}