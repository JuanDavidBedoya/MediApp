package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.dtos.UserPhoneRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserPhoneResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.entities.UserPhone;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.UserPhoneMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.PhoneRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.UserPhoneRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserPhoneService {

    private final UserPhoneRepository userPhoneRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final UserPhoneMapper userPhoneMapper;

    public UserPhoneService(UserPhoneRepository userPhoneRepository, UserRepository userRepository, PhoneRepository phoneRepository, UserPhoneMapper userPhoneMapper) {
        this.userPhoneRepository = userPhoneRepository;
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.userPhoneMapper = userPhoneMapper;
    }

    public List<UserPhoneResponseDTO> findAll() {
        return userPhoneRepository.findAll()
                .stream()
                .map(userPhoneMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserPhoneResponseDTO findById(UUID uuid) {
        UserPhone userPhone = userPhoneRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Relación usuario-teléfono no encontrada con ID: " + uuid));
        return userPhoneMapper.toResponseDTO(userPhone);
    }

    public UserPhoneResponseDTO save(UserPhoneRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.userCedula())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con cédula: " + requestDTO.userCedula()));
        Phone phone = phoneRepository.findById(requestDTO.phoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Teléfono no encontrado con ID: " + requestDTO.phoneId()));

        if (userPhoneRepository.existsByUserAndPhone(user, phone)) {
            throw new ResourceAlreadyExistsException("El usuario ya tiene asignado este número de teléfono.");
        }

        UserPhone newUserPhone = new UserPhone(user, phone);
        UserPhone savedUserPhone = userPhoneRepository.save(newUserPhone);
        return userPhoneMapper.toResponseDTO(savedUserPhone);
    }

    public void delete(UUID uuid) {
        if (!userPhoneRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("Relación usuario-teléfono no encontrada con ID: " + uuid);
        }
        userPhoneRepository.deleteById(uuid);
    }

    public UserPhoneResponseDTO update(UUID uuid, UserPhoneRequestDTO requestDTO) {
        UserPhone existingUserPhone = userPhoneRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Relación usuario-teléfono no encontrada con ID: " + uuid));

        User user = userRepository.findById(requestDTO.userCedula())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con cédula: " + requestDTO.userCedula()));
        Phone phone = phoneRepository.findById(requestDTO.phoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Teléfono no encontrado con ID: " + requestDTO.phoneId()));

        userPhoneRepository.findByUserAndPhone(user, phone).ifPresent(rel -> {
            if (!rel.getIdUserPhone().equals(uuid)) {
                 throw new ResourceAlreadyExistsException("La relación entre este usuario y teléfono ya existe.");
            }
        });
        
        existingUserPhone.setUser(user);
        existingUserPhone.setPhone(phone);
        UserPhone updatedUserPhone = userPhoneRepository.save(existingUserPhone);
        return userPhoneMapper.toResponseDTO(updatedUserPhone);
    }
}