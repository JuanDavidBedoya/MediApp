package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.SpecialityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.SpecialityMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.SpecialityRepository;
import com.mediapp.juanb.juanm.mediapp.services.SpecialityService;

@ExtendWith(MockitoExtension.class)
class SpecialityServiceTest {

    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private SpecialityMapper specialityMapper;

    @InjectMocks
    private SpecialityService specialityService;

    private List<Speciality> mockSpecialities;
    private List<SpecialityRequestDTO> specialityRequests;
    private List<SpecialityResponseDTO> specialityResponses;

    @BeforeEach
    void setUp() {
        mockSpecialities = new ArrayList<>();
        specialityRequests = new ArrayList<>();
        specialityResponses = new ArrayList<>();

        String[] names = {"Cardiología", "Pediatría", "Dermatología", "Oftalmología", "Psiquiatría"};
        for (String name : names) {
            UUID id = UUID.randomUUID();

            Speciality speciality = new Speciality();
            speciality.setIdSpeciality(id);
            speciality.setName(name);

            SpecialityRequestDTO request = new SpecialityRequestDTO(name);
            SpecialityResponseDTO response = new SpecialityResponseDTO(id, name);

            mockSpecialities.add(speciality);
            specialityRequests.add(request);
            specialityResponses.add(response);
        }
    }

    @Test
    void findAll_ReturnsAllSpecialities() {
        // Arrange
        when(specialityRepository.findAll()).thenReturn(mockSpecialities);
        when(specialityMapper.toResponseDTO(mockSpecialities.get(0))).thenReturn(specialityResponses.get(0));
        when(specialityMapper.toResponseDTO(mockSpecialities.get(1))).thenReturn(specialityResponses.get(1));
        when(specialityMapper.toResponseDTO(mockSpecialities.get(2))).thenReturn(specialityResponses.get(2));

        // Act
        List<SpecialityResponseDTO> result = specialityService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(specialityRepository, times(1)).findAll();
        verify(specialityMapper, times(3)).toResponseDTO(any(Speciality.class));
    }

    @Test
    void findById_Success() {
        // Arrange
        Speciality speciality = mockSpecialities.get(0);
        SpecialityResponseDTO response = specialityResponses.get(0);
        UUID id = speciality.getIdSpeciality();

        when(specialityRepository.findById(id)).thenReturn(Optional.of(speciality));
        when(specialityMapper.toResponseDTO(speciality)).thenReturn(response);

        // Act
        SpecialityResponseDTO result = specialityService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.idSpeciality());
        assertEquals("Cardiología", result.name());
        verify(specialityRepository, times(1)).findById(id);
    }

    @Test
    void findById_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(specialityRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> specialityService.findById(nonExistentId));
        verify(specialityMapper, never()).toResponseDTO(any(Speciality.class));
    }

    @Test
    void save_Success() {
        // Arrange
        SpecialityRequestDTO request = specialityRequests.get(0);
        Speciality specialityToSave = mockSpecialities.get(0);
        SpecialityResponseDTO response = specialityResponses.get(0);

        when(specialityRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(specialityMapper.toEntity(request)).thenReturn(specialityToSave);
        when(specialityRepository.save(specialityToSave)).thenReturn(specialityToSave);
        when(specialityMapper.toResponseDTO(specialityToSave)).thenReturn(response);

        // Act
        SpecialityResponseDTO result = specialityService.save(request);

        // Assert
        assertNotNull(result);
        assertEquals(response.idSpeciality(), result.idSpeciality());
        verify(specialityRepository, times(1)).save(specialityToSave);
    }

    @Test
    void save_Fails_NameAlreadyExists() {
        // Arrange
        SpecialityRequestDTO request = specialityRequests.get(0);
        when(specialityRepository.findByName(request.name())).thenReturn(Optional.of(mockSpecialities.get(0)));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> specialityService.save(request));
        verify(specialityRepository, never()).save(any(Speciality.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        UUID idToDelete = mockSpecialities.get(0).getIdSpeciality();
        when(specialityRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(specialityRepository).deleteById(idToDelete);

        // Act
        specialityService.delete(idToDelete);

        // Assert
        verify(specialityRepository, times(1)).existsById(idToDelete);
        verify(specialityRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(specialityRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> specialityService.delete(nonExistentId));
        verify(specialityRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void update_Success() {
        // Arrange
        Speciality existingSpeciality = mockSpecialities.get(0);
        UUID idToUpdate = existingSpeciality.getIdSpeciality();
        SpecialityRequestDTO updateRequest = new SpecialityRequestDTO("Neurocirugía");
        Speciality updatedSpeciality = new Speciality();
        updatedSpeciality.setIdSpeciality(idToUpdate);
        updatedSpeciality.setName(updateRequest.name());
        SpecialityResponseDTO response = new SpecialityResponseDTO(idToUpdate, updateRequest.name());

        when(specialityRepository.findById(idToUpdate)).thenReturn(Optional.of(existingSpeciality));
        when(specialityRepository.findByName(updateRequest.name())).thenReturn(Optional.empty());
        when(specialityRepository.save(any(Speciality.class))).thenReturn(updatedSpeciality);
        when(specialityMapper.toResponseDTO(updatedSpeciality)).thenReturn(response);

        // Act
        SpecialityResponseDTO result = specialityService.update(idToUpdate, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Neurocirugía", result.name());
        verify(specialityRepository, times(1)).findById(idToUpdate);
        verify(specialityRepository, times(1)).save(any(Speciality.class));
    }

    @Test
    void update_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        SpecialityRequestDTO updateRequest = new SpecialityRequestDTO("Test");
        when(specialityRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> specialityService.update(nonExistentId, updateRequest));
        verify(specialityRepository, never()).save(any(Speciality.class));
    }

    @Test
    void update_Fails_NameConflict() {
        // Arrange
        Speciality specialityToUpdate = mockSpecialities.get(0); // "Cardiología" con id_1
        Speciality conflictingSpeciality = mockSpecialities.get(1); // "Pediatría" con id_2
        UUID idToUpdate = specialityToUpdate.getIdSpeciality();
        
        // Intentar actualizar "Cardiología" para que se llame "Pediatría"
        SpecialityRequestDTO updateRequest = new SpecialityRequestDTO(conflictingSpeciality.getName());

        when(specialityRepository.findById(idToUpdate)).thenReturn(Optional.of(specialityToUpdate));
        when(specialityRepository.findByName(updateRequest.name())).thenReturn(Optional.of(conflictingSpeciality));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> specialityService.update(idToUpdate, updateRequest));
        verify(specialityRepository, never()).save(any(Speciality.class));
    }
}