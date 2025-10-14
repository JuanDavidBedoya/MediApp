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

import com.mediapp.juanb.juanm.mediapp.dtos.EpsRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.EpsResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Eps;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.EpsMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.EpsRepository;
import com.mediapp.juanb.juanm.mediapp.services.EpsService;

@ExtendWith(MockitoExtension.class)
class EpsServiceTest {

    @Mock
    private EpsRepository epsRepository;

    @Mock
    private EpsMapper epsMapper;

    @InjectMocks
    private EpsService epsService;

    private List<Eps> mockEpsList;
    private List<EpsRequestDTO> epsRequests;
    private List<EpsResponseDTO> epsResponses;

    @BeforeEach
    void setUp() {
        mockEpsList = new ArrayList<>();
        epsRequests = new ArrayList<>();
        epsResponses = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            UUID id = UUID.randomUUID();
            String name = "EPS " + i;

            Eps eps = new Eps();
            eps.setIdEps(id);
            eps.setName(name);

            EpsRequestDTO request = new EpsRequestDTO(name);
            EpsResponseDTO response = new EpsResponseDTO(id, name);

            mockEpsList.add(eps);
            epsRequests.add(request);
            epsResponses.add(response);
        }
    }

    @Test
    void findAll_ReturnsAllEps() {
        // Arrange
        when(epsRepository.findAll()).thenReturn(mockEpsList);
        when(epsMapper.toResponseDTO(mockEpsList.get(0))).thenReturn(epsResponses.get(0));
        when(epsMapper.toResponseDTO(mockEpsList.get(1))).thenReturn(epsResponses.get(1));
        when(epsMapper.toResponseDTO(mockEpsList.get(2))).thenReturn(epsResponses.get(2));

        // Act
        List<EpsResponseDTO> result = epsService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(epsRepository, times(1)).findAll();
        verify(epsMapper, times(3)).toResponseDTO(any(Eps.class));
    }

    @Test
    void findById_Success() {
        // Arrange
        Eps eps = mockEpsList.get(0);
        EpsResponseDTO response = epsResponses.get(0);
        UUID id = eps.getIdEps();

        when(epsRepository.findById(id)).thenReturn(Optional.of(eps));
        when(epsMapper.toResponseDTO(eps)).thenReturn(response);

        // Act
        EpsResponseDTO result = epsService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.idEps());
        assertEquals("EPS 1", result.name());
        verify(epsRepository, times(1)).findById(id);
    }

    @Test
    void findById_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(epsRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> epsService.findById(nonExistentId));
        verify(epsMapper, never()).toResponseDTO(any(Eps.class));
    }

    @Test
    void save_Success() {
        // Arrange
        EpsRequestDTO request = epsRequests.get(0);
        Eps epsToSave = mockEpsList.get(0);
        EpsResponseDTO response = epsResponses.get(0);

        when(epsRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(epsMapper.toEntity(request)).thenReturn(epsToSave);
        when(epsRepository.save(epsToSave)).thenReturn(epsToSave);
        when(epsMapper.toResponseDTO(epsToSave)).thenReturn(response);

        // Act
        EpsResponseDTO result = epsService.save(request);

        // Assert
        assertNotNull(result);
        assertEquals(response.idEps(), result.idEps());
        verify(epsRepository, times(1)).save(epsToSave);
    }

    @Test
    void save_Fails_NameAlreadyExists() {
        // Arrange
        EpsRequestDTO request = epsRequests.get(0);
        when(epsRepository.findByName(request.name())).thenReturn(Optional.of(mockEpsList.get(0)));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> epsService.save(request));
        verify(epsRepository, never()).save(any(Eps.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        UUID idToDelete = mockEpsList.get(0).getIdEps();
        when(epsRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(epsRepository).deleteById(idToDelete);

        // Act
        epsService.delete(idToDelete);

        // Assert
        verify(epsRepository, times(1)).existsById(idToDelete);
        verify(epsRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(epsRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> epsService.delete(nonExistentId));
        verify(epsRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void update_Success() {
        // Arrange
        Eps existingEps = mockEpsList.get(0);
        UUID idToUpdate = existingEps.getIdEps();
        EpsRequestDTO updateRequest = new EpsRequestDTO("Nombre Actualizado EPS");
        Eps updatedEps = new Eps();
        updatedEps.setIdEps(idToUpdate);
        updatedEps.setName(updateRequest.name());
        EpsResponseDTO response = new EpsResponseDTO(idToUpdate, updateRequest.name());

        when(epsRepository.findById(idToUpdate)).thenReturn(Optional.of(existingEps));
        when(epsRepository.findByName(updateRequest.name())).thenReturn(Optional.empty());
        when(epsRepository.save(any(Eps.class))).thenReturn(updatedEps);
        when(epsMapper.toResponseDTO(updatedEps)).thenReturn(response);

        // Act
        EpsResponseDTO result = epsService.update(idToUpdate, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Nombre Actualizado EPS", result.name());
        verify(epsRepository, times(1)).findById(idToUpdate);
        verify(epsRepository, times(1)).save(any(Eps.class));
    }

    @Test
    void update_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        EpsRequestDTO updateRequest = new EpsRequestDTO("Test");
        when(epsRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> epsService.update(nonExistentId, updateRequest));
        verify(epsRepository, never()).save(any(Eps.class));
    }

    @Test
    void update_Fails_NameConflict() {
        // Arrange
        Eps epsToUpdate = mockEpsList.get(0); // "EPS 1" con id_1
        Eps conflictingEps = mockEpsList.get(1); // "EPS 2" con id_2
        UUID idToUpdate = epsToUpdate.getIdEps();
        
        // Intentar actualizar "EPS 1" para que se llame "EPS 2"
        EpsRequestDTO updateRequest = new EpsRequestDTO(conflictingEps.getName());

        when(epsRepository.findById(idToUpdate)).thenReturn(Optional.of(epsToUpdate));
        when(epsRepository.findByName(updateRequest.name())).thenReturn(Optional.of(conflictingEps));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> epsService.update(idToUpdate, updateRequest));
        verify(epsRepository, never()).save(any(Eps.class));
    }
}