package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.dtos.PhoneRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.PhoneResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.PhoneMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.PhoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    public PhoneService(PhoneRepository phoneRepository, PhoneMapper phoneMapper) {
        this.phoneRepository = phoneRepository;
        this.phoneMapper = phoneMapper;
    }

    public List<PhoneResponseDTO> findAll() {
        return phoneRepository.findAll()
                .stream()
                .map(phoneMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PhoneResponseDTO findById(UUID uuid) {
        Phone phone = phoneRepository.findById(uuid)

                .orElseThrow(() -> new ResourceNotFoundException("Teléfono no encontrado con ID: " + uuid));
        return phoneMapper.toResponseDTO(phone);
    }

    public PhoneResponseDTO save(PhoneRequestDTO phoneDTO) {
        if (phoneRepository.findByPhone(phoneDTO.phone()).isPresent()) {

            throw new ResourceAlreadyExistsException("El número de teléfono ya está registrado: " + phoneDTO.phone());
        }
        Phone newPhone = phoneMapper.toEntity(phoneDTO);
        Phone savedPhone = phoneRepository.save(newPhone);
        return phoneMapper.toResponseDTO(savedPhone);
    }

    public void delete(UUID uuid) {
        if (!phoneRepository.existsById(uuid)) {

            throw new ResourceNotFoundException("Teléfono no encontrado con ID: " + uuid);
        }
        phoneRepository.deleteById(uuid);
    }

    public PhoneResponseDTO update(UUID uuid, PhoneRequestDTO phoneDTO) {
        Phone existingPhone = phoneRepository.findById(uuid)

                .orElseThrow(() -> new ResourceNotFoundException("Teléfono no encontrado con ID: " + uuid));

        phoneRepository.findByPhone(phoneDTO.phone()).ifPresent(phone -> {
            if (!phone.getIdPhone().equals(uuid)) {

                throw new ResourceAlreadyExistsException("El número '" + phoneDTO.phone() + "' ya está en uso.");
            }
        });

        existingPhone.setPhone(phoneDTO.phone());
        Phone updatedPhone = phoneRepository.save(existingPhone);
        return phoneMapper.toResponseDTO(updatedPhone);
    }
}