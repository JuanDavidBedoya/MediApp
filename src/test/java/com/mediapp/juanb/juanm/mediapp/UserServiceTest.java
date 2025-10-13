package com.mediapp.juanb.juanm.mediapp;

import com.mediapp.juanb.juanm.mediapp.dtos.UserRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserUpdateDTO;
import com.mediapp.juanb.juanm.mediapp.entities.*;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.UserMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.*;
import com.mediapp.juanb.juanm.mediapp.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private EpsRepository epsRepository;
    @Mock private CityRepository cityRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserPhoneRepository userPhoneRepository;
    @Mock private PhoneRepository phoneRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Eps testEps;
    private City testCity;
    private Phone testPhone;
    private UserRequestDTO userRequestDTO;
    private UserUpdateDTO userUpdateDTO;
    private UserResponseDTO userResponseDTO;
    private String testCedula;

    @BeforeEach
    void setUp() {
        testCedula = "12345";
        
        testEps = new Eps();
        testEps.setName("Test EPS");

        testCity = new City();
        testCity.setName("Test City");
        
        testPhone = new Phone("3001234567");

        testUser = new User(testCedula, "Test User", "test@example.com", "encodedPass", testCity, testEps);
        
        userRequestDTO = new UserRequestDTO(testCedula, "Test User", "test@example.com", "password", "Test EPS", List.of("3001234567"), "Test City");
        userUpdateDTO = new UserUpdateDTO("Updated User", "updated@example.com", "newPassword", "Updated EPS", "Updated City", List.of("3007654321"));
        userResponseDTO = new UserResponseDTO(testCedula, "Test User", "test@example.com", "Test EPS", List.of("3001234567"), "Test City");
    }

    @Test
    void findByCedula_Success() {
        // Arrange
        when(userRepository.findByCedula(testCedula)).thenReturn(Optional.of(testUser));
        when(userMapper.toUserResponseDTO(testUser)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.findByCedula(testCedula);

        // Assert
        assertNotNull(result);
        assertEquals(testCedula, result.cedula());
    }

    @Test
    void findByCedula_Fails_NotFound() {
        // Arrange
        when(userRepository.findByCedula(testCedula)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findByCedula(testCedula);
        });
    }

    @Test
    void save_Success() {
        // Arrange
        when(userRepository.existsById(anyString())).thenReturn(false);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(epsRepository.findByName(anyString())).thenReturn(Optional.of(testEps));
        when(cityRepository.findByName(anyString())).thenReturn(Optional.of(testCity));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(phoneRepository.findByPhone(anyString())).thenReturn(Optional.of(testPhone));
        // Mock the final findById call
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.save(userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testCedula, result.cedula());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userPhoneRepository, times(1)).save(any(UserPhone.class));
    }

    @Test
    void save_Fails_CedulaAlreadyExists() {
        // Arrange
        when(userRepository.existsById(testCedula)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.save(userRequestDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_Success() {
        // Arrange
        when(userRepository.findByCedula(testCedula)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmailAndCedulaNot(anyString(), anyString())).thenReturn(false);
        when(epsRepository.findByName(anyString())).thenReturn(Optional.of(new Eps()));
        when(cityRepository.findByName(anyString())).thenReturn(Optional.of(new City()));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPass");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.update(testCedula, userUpdateDTO);

        // Assert
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userPhoneRepository, times(1)).deleteAll(any());
        verify(userPhoneRepository, times(1)).save(any(UserPhone.class));
        verify(userRepository, times(1)).save(testUser);
    }
    
    @Test
    void update_Fails_EmailConflict() {
        // Arrange
        when(userRepository.findByCedula(testCedula)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmailAndCedulaNot(userUpdateDTO.email(), testCedula)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.update(testCedula, userUpdateDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        when(userRepository.existsById(testCedula)).thenReturn(true);
        doNothing().when(userRepository).deleteById(testCedula);

        // Act
        userService.delete(testCedula);

        // Assert
        verify(userRepository, times(1)).deleteById(testCedula);
    }

    @Test
    void delete_Fails_NotFound() {
        // Arrange
        when(userRepository.existsById(testCedula)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.delete(testCedula);
        });
        verify(userRepository, never()).deleteById(anyString());
    }
}