package com.mediapp.juanb.juanm.mediapp;

import com.mediapp.juanb.juanm.mediapp.dtos.AuthResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.LoginRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.City;
import com.mediapp.juanb.juanm.mediapp.entities.Eps;
import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.UserMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.*;
import com.mediapp.juanb.juanm.mediapp.services.AuthService;
import com.mediapp.juanb.juanm.mediapp.services.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;
    @Mock private EpsRepository epsRepository;
    @Mock private PhoneRepository phoneRepository;
    @Mock private UserPhoneRepository userPhoneRepository;
    @Mock private CityRepository cityRepository;

    @InjectMocks
    private AuthService authService;

    private UserRequestDTO userRequestDTO;
    private User testUser;
    private Eps testEps;
    private City testCity;
    private Phone testPhone;
    private UserResponseDTO userResponseDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        testEps = new Eps();
        testEps.setName("Test EPS");

        testCity = new City();
        testCity.setName("Test City");
        
        testPhone = new Phone("3001234567");

        testUser = new User("12345", "Test User", "test@example.com", "encodedPassword", testCity, testEps);

        userRequestDTO = new UserRequestDTO("12345", "Test User", "test@example.com", "password", "Test EPS", List.of("3001234567"), "Test City");
        
        userResponseDTO = new UserResponseDTO("12345", "Test User", "test@example.com", "Test EPS", List.of("3001234567"), "Test City");

        loginRequestDTO = new LoginRequestDTO("12345", "password");
    }

    // --- Pruebas para el método register ---

    @Test
    void register_Success() {
        // Arrange
        when(userRepository.findByCedula(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(cityRepository.findByName("Test City")).thenReturn(Optional.of(testCity));
        when(epsRepository.findByName("Test EPS")).thenReturn(Optional.of(testEps));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(phoneRepository.findByPhone(anyString())).thenReturn(Optional.of(testPhone));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = authService.register(userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.cedula());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userPhoneRepository, times(1)).save(any());
    }
    
    @Test
    void register_Fails_CedulaAlreadyExists() {
        // Arrange
        when(userRepository.findByCedula("12345")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            authService.register(userRequestDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_Fails_EmailAlreadyExists() {
        // Arrange
        when(userRepository.findByCedula(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            authService.register(userRequestDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void register_Fails_CityNotFound() {
        // Arrange
        when(userRepository.findByCedula(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(cityRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            authService.register(userRequestDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    // --- Pruebas para el método login ---

    @Test
    void login_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByCedula("12345")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn("dummy.jwt.token");
        when(userMapper.toUserResponseDTO(testUser)).thenReturn(userResponseDTO);

        // Act
        AuthResponseDTO result = authService.login(loginRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("dummy.jwt.token", result.token());
        assertEquals("12345", result.user().cedula());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(testUser);
    }
    
    @Test
    void login_Fails_BadCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequestDTO);
        });
        verify(jwtService, never()).generateToken(any());
    }
}