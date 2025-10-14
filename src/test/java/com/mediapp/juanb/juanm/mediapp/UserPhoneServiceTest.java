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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.mediapp.juanb.juanm.mediapp.services.UserPhoneService;

@ExtendWith(MockitoExtension.class)
class UserPhoneServiceTest {

    @Mock
    private UserPhoneRepository userPhoneRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private UserPhoneMapper userPhoneMapper;

    @InjectMocks
    private UserPhoneService userPhoneService;

    private User user1;
    private Phone phone1;
    private UserPhone userPhone1;
    private UserPhoneRequestDTO requestDTO;
    private UserPhoneResponseDTO responseDTO;
    private UUID userPhoneId;

    @BeforeEach
    void setUp() {
        // --- Entidades ---
        user1 = new User();
        user1.setCedula("12345"); // id, cedula, nombre, number
        user1.setName("Juan");

        phone1 = new Phone();
        phone1.setIdPhone(UUID.randomUUID());
        phone1.setPhone("3001112233");

        userPhoneId = UUID.randomUUID();
        userPhone1 = new UserPhone(user1, phone1);
        userPhone1.setIdUserPhone(userPhoneId);


        // --- DTOs ---
        requestDTO = new UserPhoneRequestDTO(user1.getCedula(), phone1.getIdPhone());
        responseDTO = new UserPhoneResponseDTO(userPhoneId, user1.getCedula(),user1.getName(), phone1.getIdPhone().toString());
    }

    @Test
    void findAll_Success() {
        // Arrange
        when(userPhoneRepository.findAll()).thenReturn(Collections.singletonList(userPhone1));
        when(userPhoneMapper.toResponseDTO(userPhone1)).thenReturn(responseDTO);

        // Act
        List<UserPhoneResponseDTO> result = userPhoneService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userPhoneRepository, times(1)).findAll();
        verify(userPhoneMapper, times(1)).toResponseDTO(userPhone1);
    }

    @Test
    void findById_Success() {
        // Arrange
        when(userPhoneRepository.findById(userPhoneId)).thenReturn(Optional.of(userPhone1));
        when(userPhoneMapper.toResponseDTO(userPhone1)).thenReturn(responseDTO);

        // Act
        UserPhoneResponseDTO result = userPhoneService.findById(userPhoneId);

        // Assert
        assertNotNull(result);
        assertEquals(userPhoneId, result.idUserPhone());
        verify(userPhoneRepository, times(1)).findById(userPhoneId);
    }

    @Test
    void findById_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userPhoneRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userPhoneService.findById(nonExistentId));
    }

    @Test
    void save_Success() {
        // Arrange
        when(userRepository.findById(requestDTO.userCedula())).thenReturn(Optional.of(user1));
        when(phoneRepository.findById(requestDTO.phoneId())).thenReturn(Optional.of(phone1));
        when(userPhoneRepository.existsByUserAndPhone(user1, phone1)).thenReturn(false);
        when(userPhoneRepository.save(any(UserPhone.class))).thenReturn(userPhone1);
        when(userPhoneMapper.toResponseDTO(userPhone1)).thenReturn(responseDTO);

        // Act
        UserPhoneResponseDTO result = userPhoneService.save(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(userPhoneRepository, times(1)).save(any(UserPhone.class));
    }

    @Test
    void save_Fails_UserNotFound() {
        // Arrange
        when(userRepository.findById(requestDTO.userCedula())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userPhoneService.save(requestDTO));
        verify(userPhoneRepository, never()).save(any(UserPhone.class));
    }

    @Test
    void save_Fails_PhoneNotFound() {
        // Arrange
        when(userRepository.findById(requestDTO.userCedula())).thenReturn(Optional.of(user1));
        when(phoneRepository.findById(requestDTO.phoneId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userPhoneService.save(requestDTO));
        verify(userPhoneRepository, never()).save(any(UserPhone.class));
    }

    @Test
    void save_Fails_RelationAlreadyExists() {
        // Arrange
        when(userRepository.findById(requestDTO.userCedula())).thenReturn(Optional.of(user1));
        when(phoneRepository.findById(requestDTO.phoneId())).thenReturn(Optional.of(phone1));
        when(userPhoneRepository.existsByUserAndPhone(user1, phone1)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userPhoneService.save(requestDTO));
        verify(userPhoneRepository, never()).save(any(UserPhone.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        when(userPhoneRepository.existsById(userPhoneId)).thenReturn(true);
        doNothing().when(userPhoneRepository).deleteById(userPhoneId);

        // Act
        userPhoneService.delete(userPhoneId);

        // Assert
        verify(userPhoneRepository, times(1)).existsById(userPhoneId);
        verify(userPhoneRepository, times(1)).deleteById(userPhoneId);
    }

    @Test
    void delete_Fails_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userPhoneRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userPhoneService.delete(nonExistentId));
        verify(userPhoneRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void update_Success() {
        // Arrange
        User newUser = new User();
        newUser.setCedula("54321");
        Phone newPhone = new Phone();
        newPhone.setIdPhone(UUID.randomUUID());
        UserPhoneRequestDTO updateRequest = new UserPhoneRequestDTO(newUser.getCedula(), newPhone.getIdPhone());
        
        when(userPhoneRepository.findById(userPhoneId)).thenReturn(Optional.of(userPhone1));
        when(userRepository.findById(newUser.getCedula())).thenReturn(Optional.of(newUser));
        when(phoneRepository.findById(newPhone.getIdPhone())).thenReturn(Optional.of(newPhone));
        when(userPhoneRepository.findByUserAndPhone(newUser, newPhone)).thenReturn(Optional.empty());
        when(userPhoneRepository.save(any(UserPhone.class))).thenReturn(userPhone1); // Re-using for simplicity
        when(userPhoneMapper.toResponseDTO(userPhone1)).thenReturn(responseDTO);

        // Act
        UserPhoneResponseDTO result = userPhoneService.update(userPhoneId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(userPhoneRepository, times(1)).save(any(UserPhone.class));
    }

    @Test
    void update_Fails_RelationNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userPhoneRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userPhoneService.update(nonExistentId, requestDTO));
        verify(userPhoneRepository, never()).save(any(UserPhone.class));
    }

    @Test
    void update_Fails_ConflictWithExistingRelation() {
        // Arrange
        User user2 = new User(); user2.setCedula("98765");
        Phone phone2 = new Phone(); phone2.setIdPhone(UUID.randomUUID());
        UserPhone conflictingRelation = new UserPhone(user2, phone2);
        conflictingRelation.setIdUserPhone(UUID.randomUUID()); // Different ID

        UserPhoneRequestDTO updateRequest = new UserPhoneRequestDTO(user2.getCedula(), phone2.getIdPhone());

        when(userPhoneRepository.findById(userPhoneId)).thenReturn(Optional.of(userPhone1));
        when(userRepository.findById(user2.getCedula())).thenReturn(Optional.of(user2));
        when(phoneRepository.findById(phone2.getIdPhone())).thenReturn(Optional.of(phone2));
        when(userPhoneRepository.findByUserAndPhone(user2, phone2)).thenReturn(Optional.of(conflictingRelation));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userPhoneService.update(userPhoneId, updateRequest));
        verify(userPhoneRepository, never()).save(any(UserPhone.class));
    }
}