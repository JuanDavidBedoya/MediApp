package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Receptionist;
import com.mediapp.juanb.juanm.mediapp.repositories.ReceptionistRepository;

@Service
public class ReceptionistService {

    private ReceptionistRepository receptionistRepository;

    public ReceptionistService(ReceptionistRepository receptionistRepository) {
        this.receptionistRepository = receptionistRepository;
    }

    public List<Receptionist> findAll() {
        return receptionistRepository.findAll();
    }

    public Optional<Receptionist> findById(String cedula) {
        return receptionistRepository.findById(cedula);
    }

    public Receptionist save(Receptionist receptionist) {
        return receptionistRepository.save(receptionist);
    }

    public void delete(String cedula) {
        receptionistRepository.deleteById(cedula);
    }

    public Receptionist update(String cedula, Receptionist receptionist) {
        receptionist.setCedula(cedula);
        return receptionistRepository.save(receptionist);
    }
}
