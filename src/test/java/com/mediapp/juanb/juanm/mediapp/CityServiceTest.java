package com.mediapp.juanb.juanm.mediapp;

import com.mediapp.juanb.juanm.mediapp.dtos.CityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.CityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.City;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.CityMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.CityRepository;
import com.mediapp.juanb.juanm.mediapp.services.CityService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityService cityService;

    private City testCity;
    private CityRequestDTO cityRequestDTO;
    private CityResponseDTO cityResponseDTO;
    private UUID testUuid;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        
        testCity = new City();
        testCity.setIdCity(testUuid);
        testCity.setName("Armenia");

        cityRequestDTO = new CityRequestDTO("Armenia");
        cityResponseDTO = new CityResponseDTO(testUuid, "Armenia");
    }

    @Test
    void findAll_ReturnsListOfCities() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(Collections.singletonList(testCity));
        when(cityMapper.toResponseDTO(any(City.class))).thenReturn(cityResponseDTO);

        // Act
        List<CityResponseDTO> result = cityService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Armenia", result.get(0).name());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void findById_Success() {
        // Arrange
        when(cityRepository.findById(testUuid)).thenReturn(Optional.of(testCity));
        when(cityMapper.toResponseDTO(testCity)).thenReturn(cityResponseDTO);

        // Act
        CityResponseDTO result = cityService.findById(testUuid);

        // Assert
        assertNotNull(result);
        assertEquals("Armenia", result.name());
    }

    @Test
    void findById_Fails_NotFound() {
        // Arrange
        when(cityRepository.findById(testUuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cityService.findById(testUuid);
        });
    }

    @Test
    void save_Success() {
        // Arrange
        when(cityRepository.findByName("Armenia")).thenReturn(Optional.empty());
        when(cityMapper.toEntity(cityRequestDTO)).thenReturn(testCity);
        when(cityRepository.save(any(City.class))).thenReturn(testCity);
        when(cityMapper.toResponseDTO(testCity)).thenReturn(cityResponseDTO);

        // Act
        CityResponseDTO result = cityService.save(cityRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Armenia", result.name());
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    void save_Fails_NameAlreadyExists() {
        // Arrange
        when(cityRepository.findByName("Armenia")).thenReturn(Optional.of(testCity));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            cityService.save(cityRequestDTO);
        });
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void update_Success() {
        // Arrange
        CityRequestDTO updateRequest = new CityRequestDTO("Yerevan");
        when(cityRepository.findById(testUuid)).thenReturn(Optional.of(testCity));
        when(cityRepository.findByName("Yerevan")).thenReturn(Optional.empty());
        when(cityRepository.save(any(City.class))).thenReturn(testCity);
        when(cityMapper.toResponseDTO(testCity)).thenReturn(new CityResponseDTO(testUuid, "Yerevan"));

        // Act
        CityResponseDTO result = cityService.update(testUuid, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Yerevan", result.name()); // El mapper devuelve el nuevo nombre
        assertEquals("Yerevan", testCity.getName()); // Se verifica que el nombre en la entidad fue modificado
        verify(cityRepository, times(1)).save(testCity);
    }
    
    @Test
    void update_Fails_NotFound() {
        // Arrange
        when(cityRepository.findById(testUuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cityService.update(testUuid, cityRequestDTO);
        });
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void update_Fails_NameConflict() {
        // Arrange
        City conflictingCity = new City();
        conflictingCity.setIdCity(UUID.randomUUID());
        conflictingCity.setName("Yerevan");
        
        CityRequestDTO updateRequest = new CityRequestDTO("Yerevan");

        when(cityRepository.findById(testUuid)).thenReturn(Optional.of(testCity));
        when(cityRepository.findByName("Yerevan")).thenReturn(Optional.of(conflictingCity));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            cityService.update(testUuid, updateRequest);
        });
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        when(cityRepository.existsById(testUuid)).thenReturn(true);

        // Act
        cityService.delete(testUuid);

        // Assert
        verify(cityRepository, times(1)).deleteById(testUuid);
    }

    @Test
    void delete_Fails_NotFound() {
        // Arrange
        when(cityRepository.existsById(testUuid)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cityService.delete(testUuid);
        });
        verify(cityRepository, never()).deleteById(any(UUID.class));
    }
}