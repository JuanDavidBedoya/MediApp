package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Eps;
import com.mediapp.juanb.juanm.mediapp.repositories.EpsRepository;

@Service
public class EpsService {

    private EpsRepository epsRepository;

    public EpsService(EpsRepository epsRepository) {
        this.epsRepository = epsRepository;
    }

    public List<Eps> findAll() {
        return epsRepository.findAll();
    }

    public Optional<Eps> findById(UUID uuid) {
        return epsRepository.findById(uuid);
    }

    public Eps save(Eps eps) {
        return epsRepository.save(eps);
    }

    public void delete(UUID uuid) {
        epsRepository.deleteById(uuid);
    }

    public Eps update(UUID uuid, Eps eps) {
        eps.setIdEps(uuid);
        return epsRepository.save(eps);
    }
}