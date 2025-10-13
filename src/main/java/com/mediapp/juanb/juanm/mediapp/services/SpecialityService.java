package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.SpecialityMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.SpecialityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpecialityService {

    private final SpecialityRepository specialityRepository;
    private final SpecialityMapper specialityMapper;

    public SpecialityService(SpecialityRepository specialityRepository, SpecialityMapper specialityMapper) {
        this.specialityRepository = specialityRepository;
        this.specialityMapper = specialityMapper;
    }

    public List<SpecialityResponseDTO> findAll() {
        return specialityRepository.findAll()
                .stream()
                .map(specialityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SpecialityResponseDTO findById(UUID uuid) {
        Speciality speciality = specialityRepository.findById(uuid)

                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con ID: " + uuid));
        return specialityMapper.toResponseDTO(speciality);
    }

    public SpecialityResponseDTO save(SpecialityRequestDTO specialityDTO) {
        if (specialityRepository.findByName(specialityDTO.name()).isPresent()) {

            throw new ResourceAlreadyExistsException("Ya existe una especialidad con el nombre: " + specialityDTO.name());
        }
        Speciality newSpeciality = specialityMapper.toEntity(specialityDTO);
        Speciality savedSpeciality = specialityRepository.save(newSpeciality);
        return specialityMapper.toResponseDTO(savedSpeciality);
    }

    public void delete(UUID uuid) {
        if (!specialityRepository.existsById(uuid)) {

            throw new ResourceNotFoundException("Especialidad no encontrada con ID: " + uuid);
        }
        specialityRepository.deleteById(uuid);
    }

    public SpecialityResponseDTO update(UUID uuid, SpecialityRequestDTO specialityDTO) {
        Speciality existingSpeciality = specialityRepository.findById(uuid)

                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con ID: " + uuid));

        specialityRepository.findByName(specialityDTO.name()).ifPresent(speciality -> {
            if (!speciality.getIdSpeciality().equals(uuid)) {

                throw new ResourceAlreadyExistsException("El nombre '" + specialityDTO.name() + "' ya está en uso por otra especialidad.");
            }
        });

        existingSpeciality.setName(specialityDTO.name());
        Speciality updatedSpeciality = specialityRepository.save(existingSpeciality);
        return specialityMapper.toResponseDTO(updatedSpeciality);
    }
}