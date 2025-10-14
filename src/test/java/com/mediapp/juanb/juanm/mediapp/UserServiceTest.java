package com.mediapp.juanb.juanm.mediapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mediapp.juanb.juanm.mediapp.dtos.UserRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserUpdateDTO;
import com.mediapp.juanb.juanm.mediapp.entities.City;
import com.mediapp.juanb.juanm.mediapp.entities.Eps;
import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.entities.UserPhone;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.UserMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.CityRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.EpsRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.PhoneRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.UserPhoneRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.UserRepository;
import com.mediapp.juanb.juanm.mediapp.services.UserService;

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

    private User user;
    private Eps eps;
    private City city;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        eps = new Eps();
        eps.setIdEps(UUID.randomUUID());
        eps.setName("SaludTotal");

        city = new City();
        city.setIdCity(UUID.randomUUID());
        city.setName("Bogotá");

        user = new User();
        user.setCedula("123456789");
        user.setName("Juan Perez");
        user.setEmail("juan.perez@test.com");
        user.setPassword("hashedPassword");
        user.setEps(eps);
        user.setCity(city);

        // --- ACTUALIZADO ---
        // Corregido el constructor de UserRequestDTO para coincidir con la nueva definición (phones entre eps y city).
        userRequestDTO = new UserRequestDTO(
            "123456789", 
            "Juan Perez", 
            "juan.perez@test.com", 
            "password123", 
            "SaludTotal", 
            List.of("3001234567"), // Se añade la lista de teléfonos
            "Bogotá"
        );

        // --- ACTUALIZADO ---
        // Corregido el constructor de UserResponseDTO para coincidir con el orden de campos definido.
        userResponseDTO = new UserResponseDTO(
            "123456789", 
            "Juan Perez", 
            "juan.perez@test.com", 
            "SaludTotal", 
            List.of("3001234567"), // Se añade la lista de teléfonos
            "Bogotá"
        );
    }

    @Test
    void findByCedula_Success() {
        // Arrange
        when(userRepository.findByCedula(user.getCedula())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.findByCedula(user.getCedula());

        // Assert
        assertNotNull(result);
        assertEquals(user.getCedula(), result.cedula());
        verify(userRepository, times(1)).findByCedula(user.getCedula());
    }

    @Test
    void findByCedula_Fails_NotFound() {
        // Arrange
        String cedula = "00000";
        when(userRepository.findByCedula(cedula)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findByCedula(cedula));
    }

    @Test
    void loadUserByUsername_Success() {
        // Arrange
        when(userRepository.findByCedula(user.getCedula())).thenReturn(Optional.of(user));
        
        // Act
        UserDetails userDetails = userService.loadUserByUsername(user.getCedula());

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getCedula(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_Fails_NotFound() {
        // Arrange
        String cedula = "00000";
        when(userRepository.findByCedula(cedula)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(cedula));
    }

    @Test
    void save_Success() {
        // Arrange
        when(userRepository.existsById(userRequestDTO.cedula())).thenReturn(false);
        when(userRepository.findByEmail(userRequestDTO.email())).thenReturn(Optional.empty());
        when(epsRepository.findByName(userRequestDTO.epsName())).thenReturn(Optional.of(eps));
        when(cityRepository.findByName(userRequestDTO.cityName())).thenReturn(Optional.of(city));
        when(passwordEncoder.encode(userRequestDTO.contrasena())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponseDTO);
        
        // --- AÑADIDO ---
        // Simular la lógica de búsqueda y guardado de teléfonos
        when(phoneRepository.findByPhone(anyString())).thenReturn(Optional.empty()); // Simula que el teléfono es nuevo
        when(phoneRepository.save(any(Phone.class))).thenReturn(new Phone());
        when(userPhoneRepository.save(any(UserPhone.class))).thenReturn(new UserPhone());


        // Act
        UserResponseDTO result = userService.save(userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userRequestDTO.cedula(), result.cedula());
        verify(userRepository, times(1)).save(any(User.class));
        verify(phoneRepository, times(1)).save(any(Phone.class)); // Verifica que se guarde el nuevo teléfono
        verify(userPhoneRepository, times(1)).save(any(UserPhone.class)); // Verifica que se cree la relación
    }

    @Test
    void save_Fails_CedulaAlreadyExists() {
        // Arrange
        when(userRepository.existsById(userRequestDTO.cedula())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.save(userRequestDTO));
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void save_Fails_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsById(userRequestDTO.cedula())).thenReturn(false);
        when(userRepository.findByEmail(userRequestDTO.email())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.save(userRequestDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void save_Fails_EpsNotFound() {
        // Arrange
        when(userRepository.existsById(userRequestDTO.cedula())).thenReturn(false);
        when(userRepository.findByEmail(userRequestDTO.email())).thenReturn(Optional.empty());
        when(epsRepository.findByName(userRequestDTO.epsName())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.save(userRequestDTO));
    }
    
    @Test
    void update_Success_WithPasswordAndPhones() {
        // Arrange
        String cedula = user.getCedula();
        // --- ACTUALIZADO ---
        // Constructor de UserUpdateDTO corregido para coincidir con el orden de campos.
        UserUpdateDTO updateDTO = new UserUpdateDTO(
            "Juan Carlos Perez", 
            "jc.perez@test.com", 
            "nuevaPassword", 
            "NuevaEPS", 
            "Medellín", 
            List.of("3001234567")
        );
        
        when(userRepository.findByCedula(cedula)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndCedulaNot(updateDTO.email(), cedula)).thenReturn(false);
        when(epsRepository.findByName(anyString())).thenReturn(Optional.of(new Eps()));
        when(cityRepository.findByName(anyString())).thenReturn(Optional.of(new City()));
        when(passwordEncoder.encode(updateDTO.contrasena())).thenReturn("newHashedPassword");
        when(phoneRepository.findByPhone(anyString())).thenReturn(Optional.of(new Phone("3001234567")));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.update(cedula, updateDTO);

        // Assert
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode(updateDTO.contrasena());
        verify(userPhoneRepository, times(1)).deleteAll(any());
        verify(userPhoneRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_Success_WithoutPassword() {
        // Arrange
        String cedula = user.getCedula();
        UserUpdateDTO updateDTO = new UserUpdateDTO("Juan Carlos Perez", "jc.perez@test.com", "", "NuevaEPS", "Medellín", null);

        when(userRepository.findByCedula(cedula)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndCedulaNot(updateDTO.email(), cedula)).thenReturn(false);
        when(epsRepository.findByName(anyString())).thenReturn(Optional.of(new Eps()));
        when(cityRepository.findByName(anyString())).thenReturn(Optional.of(new City()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.update(cedula, updateDTO);

        // Assert
        assertNotNull(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_Fails_UserNotFound() {
        // Arrange
        String cedula = "00000";
        UserUpdateDTO updateDTO = new UserUpdateDTO("Test", "test@test.com", null, "EPS", "City", null);
        when(userRepository.findByCedula(cedula)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.update(cedula, updateDTO));
    }
    
    @Test
    void update_Fails_EmailConflict() {
        // Arrange
        String cedula = user.getCedula();
        UserUpdateDTO updateDTO = new UserUpdateDTO("Test", "otro@test.com", null, "EPS", "City", null);
        when(userRepository.findByCedula(cedula)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndCedulaNot(updateDTO.email(), cedula)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.update(cedula, updateDTO));
    }
}