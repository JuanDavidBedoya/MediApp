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

import com.mediapp.juanb.juanm.mediapp.dtos.PhoneRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.PhoneResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.PhoneMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.PhoneRepository;
import com.mediapp.juanb.juanm.mediapp.services.PhoneService;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTest {

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private PhoneMapper phoneMapper;

    @InjectMocks
    private PhoneService phoneService;

    private List<Phone> mockPhones;
    private List<PhoneRequestDTO> phoneRequests;
    private List<PhoneResponseDTO> phoneResponses;

    @BeforeEach
    void setUp() {
        mockPhones = new ArrayList<>();
        phoneRequests = new ArrayList<>();
        phoneResponses = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            UUID id = UUID.randomUUID();
            String phoneNumber = "300123456" + i;

            Phone phone = new Phone();
            phone.setIdPhone(id);
            phone.setPhone(phoneNumber);

            PhoneRequestDTO request = new PhoneRequestDTO(phoneNumber);
            PhoneResponseDTO response = new PhoneResponseDTO(id, phoneNumber);

            mockPhones.add(phone);
            phoneRequests.add(request);
            phoneResponses.add(response);
        }
    }

    @Test
    void findAll_ReturnsAllPhones() {

        when(phoneRepository.findAll()).thenReturn(mockPhones);
        when(phoneMapper.toResponseDTO(mockPhones.get(0))).thenReturn(phoneResponses.get(0));
        when(phoneMapper.toResponseDTO(mockPhones.get(1))).thenReturn(phoneResponses.get(1));
        when(phoneMapper.toResponseDTO(mockPhones.get(2))).thenReturn(phoneResponses.get(2));

        List<PhoneResponseDTO> result = phoneService.findAll();

        assertNotNull(result);
        assertEquals(5, result.size());
        verify(phoneRepository, times(1)).findAll();
        verify(phoneMapper, times(3)).toResponseDTO(any(Phone.class));
    }

    @Test
    void findById_Success() {

        Phone phone = mockPhones.get(0);
        PhoneResponseDTO response = phoneResponses.get(0);
        UUID id = phone.getIdPhone();

        when(phoneRepository.findById(id)).thenReturn(Optional.of(phone));
        when(phoneMapper.toResponseDTO(phone)).thenReturn(response);

        PhoneResponseDTO result = phoneService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.idPhone());
        assertEquals("3001234561", result.phone());
        verify(phoneRepository, times(1)).findById(id);
    }

    @Test
    void findById_Fails_NotFound() {

        UUID nonExistentId = UUID.randomUUID();
        when(phoneRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> phoneService.findById(nonExistentId));
        verify(phoneMapper, never()).toResponseDTO(any(Phone.class));
    }

    @Test
    void save_Success() {

        PhoneRequestDTO request = phoneRequests.get(0);
        Phone phoneToSave = mockPhones.get(0);
        PhoneResponseDTO response = phoneResponses.get(0);

        when(phoneRepository.findByPhone(request.phone())).thenReturn(Optional.empty());
        when(phoneMapper.toEntity(request)).thenReturn(phoneToSave);
        when(phoneRepository.save(phoneToSave)).thenReturn(phoneToSave);
        when(phoneMapper.toResponseDTO(phoneToSave)).thenReturn(response);

        PhoneResponseDTO result = phoneService.save(request);

        assertNotNull(result);
        assertEquals(response.idPhone(), result.idPhone());
        verify(phoneRepository, times(1)).save(phoneToSave);
    }

    @Test
    void save_Fails_PhoneAlreadyExists() {

        PhoneRequestDTO request = phoneRequests.get(0);
        when(phoneRepository.findByPhone(request.phone())).thenReturn(Optional.of(mockPhones.get(0)));

        assertThrows(ResourceAlreadyExistsException.class, () -> phoneService.save(request));
        verify(phoneRepository, never()).save(any(Phone.class));
    }

    @Test
    void delete_Success() {

        UUID idToDelete = mockPhones.get(0).getIdPhone();
        when(phoneRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(phoneRepository).deleteById(idToDelete);

        phoneService.delete(idToDelete);

        verify(phoneRepository, times(1)).existsById(idToDelete);
        verify(phoneRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void delete_Fails_NotFound() {

        UUID nonExistentId = UUID.randomUUID();
        when(phoneRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> phoneService.delete(nonExistentId));
        verify(phoneRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void update_Success() {

        Phone existingPhone = mockPhones.get(0);
        UUID idToUpdate = existingPhone.getIdPhone();
        PhoneRequestDTO updateRequest = new PhoneRequestDTO("3109876543");
        Phone updatedPhone = new Phone();
        updatedPhone.setIdPhone(idToUpdate);
        updatedPhone.setPhone(updateRequest.phone());
        PhoneResponseDTO response = new PhoneResponseDTO(idToUpdate, updateRequest.phone());

        when(phoneRepository.findById(idToUpdate)).thenReturn(Optional.of(existingPhone));
        when(phoneRepository.findByPhone(updateRequest.phone())).thenReturn(Optional.empty());
        when(phoneRepository.save(any(Phone.class))).thenReturn(updatedPhone);
        when(phoneMapper.toResponseDTO(updatedPhone)).thenReturn(response);

        PhoneResponseDTO result = phoneService.update(idToUpdate, updateRequest);

        assertNotNull(result);
        assertEquals("3109876543", result.phone());
        verify(phoneRepository, times(1)).findById(idToUpdate);
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }

    @Test
    void update_Fails_NotFound() {

        UUID nonExistentId = UUID.randomUUID();
        PhoneRequestDTO updateRequest = new PhoneRequestDTO("Test");
        when(phoneRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> phoneService.update(nonExistentId, updateRequest));
        verify(phoneRepository, never()).save(any(Phone.class));
    }

    @Test
    void update_Fails_PhoneConflict() {

        Phone phoneToUpdate = mockPhones.get(0); 
        Phone conflictingPhone = mockPhones.get(1); 
        UUID idToUpdate = phoneToUpdate.getIdPhone();
     
        PhoneRequestDTO updateRequest = new PhoneRequestDTO(conflictingPhone.getPhone());

        when(phoneRepository.findById(idToUpdate)).thenReturn(Optional.of(phoneToUpdate));
        when(phoneRepository.findByPhone(updateRequest.phone())).thenReturn(Optional.of(conflictingPhone));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> phoneService.update(idToUpdate, updateRequest));
        verify(phoneRepository, never()).save(any(Phone.class));
    }
}