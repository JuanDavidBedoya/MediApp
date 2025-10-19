package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mediapp.juanb.juanm.mediapp.dtos.CityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.CityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.City;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.CityMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.CityRepository;
import com.mediapp.juanb.juanm.mediapp.services.CityService;

import io.jsonwebtoken.lang.Collections;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityService cityService;

    private List<City> mockCities;
    private List<CityRequestDTO> cityRequests;
    private List<CityResponseDTO> cityResponses;

    @BeforeEach
    void setUp() {
        mockCities = new ArrayList<>();
        cityRequests = new ArrayList<>();
        cityResponses = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            UUID id = UUID.randomUUID();
            String name = "Ciudad " + i;

            City city = new City();
            city.setIdCity(id);
            city.setName(name);

            CityRequestDTO request = new CityRequestDTO(name);
            CityResponseDTO response = new CityResponseDTO(id, name);

            mockCities.add(city);
            cityRequests.add(request);
            cityResponses.add(response);
        }
    }

    @Test
    void findAll_ReturnsAllCities() {
  
        when(cityRepository.findAll()).thenReturn(mockCities);
        when(cityMapper.toResponseDTO(mockCities.get(0))).thenReturn(cityResponses.get(0));
        when(cityMapper.toResponseDTO(mockCities.get(1))).thenReturn(cityResponses.get(1));
        when(cityMapper.toResponseDTO(mockCities.get(2))).thenReturn(cityResponses.get(2));

        List<CityResponseDTO> result = cityService.findAll();

        assertNotNull(result);
        assertEquals(5, result.size());
        verify(cityRepository, times(1)).findAll();
        verify(cityMapper, times(5)).toResponseDTO(any(City.class));
    }

    @Test
    void findById_Success() {

        City city = mockCities.get(0);
        CityResponseDTO response = cityResponses.get(0);
        UUID id = city.getIdCity();

        when(cityRepository.findById(id)).thenReturn(Optional.of(city));
        when(cityMapper.toResponseDTO(city)).thenReturn(response);
        CityResponseDTO result = cityService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.idCity());
        assertEquals("Ciudad 1", result.name());
        verify(cityRepository, times(1)).findById(id);
    }

    @Test
    void findById_Fails_NotFound() {

        UUID nonExistentId = UUID.randomUUID();
        when(cityRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.findById(nonExistentId));
        verify(cityMapper, never()).toResponseDTO(any(City.class));
    }

    @Test
    void findAll_ReturnsEmptyList() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        List<CityResponseDTO> result = cityService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void save_Success() {

        CityRequestDTO request = cityRequests.get(0);
        City cityToSave = mockCities.get(0);
        CityResponseDTO response = cityResponses.get(0);

        when(cityRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(cityMapper.toEntity(request)).thenReturn(cityToSave);
        when(cityRepository.save(cityToSave)).thenReturn(cityToSave);
        when(cityMapper.toResponseDTO(cityToSave)).thenReturn(response);

        CityResponseDTO result = cityService.save(request);

        assertNotNull(result);
        assertEquals(response.idCity(), result.idCity());
        verify(cityRepository, times(1)).save(cityToSave);
    }

    @Test
    void save_Fails_NameAlreadyExists() {

        CityRequestDTO request = cityRequests.get(0);
        when(cityRepository.findByName(request.name())).thenReturn(Optional.of(mockCities.get(0)));

        assertThrows(ResourceAlreadyExistsException.class, () -> cityService.save(request));
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void delete_Success() {
        
        UUID idToDelete = mockCities.get(0).getIdCity();
        when(cityRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(cityRepository).deleteById(idToDelete);

        cityService.delete(idToDelete);

        verify(cityRepository, times(1)).existsById(idToDelete);
        verify(cityRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_Fails_NotFound() {

        UUID nonExistentId = UUID.randomUUID();
        when(cityRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cityService.delete(nonExistentId));
        verify(cityRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void update_Success() {

        City existingCity = mockCities.get(0);
        UUID idToUpdate = existingCity.getIdCity();
        CityRequestDTO updateRequest = new CityRequestDTO("Nombre Actualizado");
        City updatedCity = new City();
        updatedCity.setIdCity(idToUpdate);
        updatedCity.setName(updateRequest.name());
        CityResponseDTO response = new CityResponseDTO(idToUpdate, updateRequest.name());

        when(cityRepository.findById(idToUpdate)).thenReturn(Optional.of(existingCity));
        when(cityRepository.findByName(updateRequest.name())).thenReturn(Optional.empty());
        when(cityRepository.save(any(City.class))).thenReturn(updatedCity);
        when(cityMapper.toResponseDTO(updatedCity)).thenReturn(response);

        CityResponseDTO result = cityService.update(idToUpdate, updateRequest);

        assertNotNull(result);
        assertEquals("Nombre Actualizado", result.name());
        verify(cityRepository, times(1)).findById(idToUpdate);
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    void update_Fails_NotFound() {

        UUID nonExistentId = UUID.randomUUID();
        CityRequestDTO updateRequest = new CityRequestDTO("Test");
        when(cityRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.update(nonExistentId, updateRequest));
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void update_Fails_NameConflict() {

        City cityToUpdate = mockCities.get(0); 
        City conflictingCity = mockCities.get(1); 
        UUID idToUpdate = cityToUpdate.getIdCity();

        CityRequestDTO updateRequest = new CityRequestDTO(conflictingCity.getName());

        when(cityRepository.findById(idToUpdate)).thenReturn(Optional.of(cityToUpdate));
        when(cityRepository.findByName(updateRequest.name())).thenReturn(Optional.of(conflictingCity));

        assertThrows(ResourceAlreadyExistsException.class, () -> cityService.update(idToUpdate, updateRequest));
        verify(cityRepository, never()).save(any(City.class));
    }
}