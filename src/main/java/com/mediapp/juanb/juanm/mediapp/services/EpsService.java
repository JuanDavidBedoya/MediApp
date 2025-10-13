package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.dtos.EpsRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.EpsResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Eps;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.EpsMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.EpsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EpsService {

    private final EpsRepository epsRepository;
    private final EpsMapper epsMapper;

    public EpsService(EpsRepository epsRepository, EpsMapper epsMapper) {
        this.epsRepository = epsRepository;
        this.epsMapper = epsMapper;
    }

    public List<EpsResponseDTO> findAll() {
        return epsRepository.findAll()
                .stream()
                .map(epsMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EpsResponseDTO findById(UUID uuid) {
        Eps eps = epsRepository.findById(uuid)

                .orElseThrow(() -> new ResourceNotFoundException("EPS no encontrada con ID: " + uuid));
        return epsMapper.toResponseDTO(eps);
    }

    public EpsResponseDTO save(EpsRequestDTO epsDTO) {
        if (epsRepository.findByName(epsDTO.name()).isPresent()) {

            throw new ResourceAlreadyExistsException("Ya existe una EPS con el nombre: " + epsDTO.name());
        }
        Eps newEps = epsMapper.toEntity(epsDTO);
        Eps savedEps = epsRepository.save(newEps);
        return epsMapper.toResponseDTO(savedEps);
    }

    public void delete(UUID uuid) {
        if (!epsRepository.existsById(uuid)) {

            throw new ResourceNotFoundException("EPS no encontrada con ID: " + uuid);
        }
        epsRepository.deleteById(uuid);
    }

    public EpsResponseDTO update(UUID uuid, EpsRequestDTO epsDTO) {
        Eps existingEps = epsRepository.findById(uuid)

                .orElseThrow(() -> new ResourceNotFoundException("EPS no encontrada con ID: " + uuid));
        
        epsRepository.findByName(epsDTO.name()).ifPresent(eps -> {
            if (!eps.getIdEps().equals(uuid)) {

                throw new ResourceAlreadyExistsException("El nombre '" + epsDTO.name() + "' ya está en uso por otra EPS.");
            }
        });

        existingEps.setName(epsDTO.name());
        Eps updatedEps = epsRepository.save(existingEps);
        return epsMapper.toResponseDTO(updatedEps);
    }
}