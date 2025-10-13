package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.dtos.CityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.CityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.City;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.CityMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public List<CityResponseDTO> findAll() {
        return cityRepository.findAll()
                .stream()
                .map(cityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CityResponseDTO findById(UUID uuid) {
        City city = cityRepository.findById(uuid)
  
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada con ID: " + uuid));
        return cityMapper.toResponseDTO(city);
    }

    public CityResponseDTO save(CityRequestDTO cityDTO) {
        if (cityRepository.findByName(cityDTO.name()).isPresent()) {
  
            throw new ResourceAlreadyExistsException("Ya existe una ciudad con el nombre: " + cityDTO.name());
        }
        City newCity = cityMapper.toEntity(cityDTO);
        City savedCity = cityRepository.save(newCity);
        return cityMapper.toResponseDTO(savedCity);
    }

    public void delete(UUID uuid) {
        if (!cityRepository.existsById(uuid)) {

            throw new ResourceNotFoundException("Ciudad no encontrada con ID: " + uuid);
        }
        cityRepository.deleteById(uuid);
    }

    public CityResponseDTO update(UUID uuid, CityRequestDTO cityDTO) {
        City existingCity = cityRepository.findById(uuid)
  
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada con ID: " + uuid));

        cityRepository.findByName(cityDTO.name()).ifPresent(city -> {
            if (!city.getIdCity().equals(uuid)) {

                throw new ResourceAlreadyExistsException("El nombre '" + cityDTO.name() + "' ya está en uso por otra ciudad.");
            }
        });

        existingCity.setName(cityDTO.name());
        City updatedCity = cityRepository.save(existingCity);
        return cityMapper.toResponseDTO(updatedCity);
    }
}