package com.mediapp.juanb.juanm.mediapp;

import com.mediapp.juanb.juanm.mediapp.dtos.DoctorRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.DoctorResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.DoctorMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.DoctorRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.SpecialityRepository;
import com.mediapp.juanb.juanm.mediapp.services.DoctorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor testDoctor;
    private Speciality testSpeciality;
    private DoctorRequestDTO doctorRequestDTO;
    private DoctorResponseDTO doctorResponseDTO;
    private String testCedula;

    @BeforeEach
    void setUp() {
        testCedula = "12345678";

        testSpeciality = new Speciality();
        testSpeciality.setName("Cardiología");

        testDoctor = new Doctor();
        testDoctor.setCedulaDoctor(testCedula);
        testDoctor.setName("Dr. Juan Pérez");
        testDoctor.setEmail("juan.perez@example.com");
        testDoctor.setPhone("3001112233");
        testDoctor.setSpeciality(testSpeciality);

        doctorRequestDTO = new DoctorRequestDTO(testCedula, "Dr. Juan Pérez", "3001112233", "juan.perez@example.com", "Cardiología");
        doctorResponseDTO = new DoctorResponseDTO(testCedula, "Dr. Juan Pérez", "3001112233", "juan.perez@example.com", "Cardiología");
    }

    @Test
    void findAll_ReturnsListOfDoctors() {
        // Arrange
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(testDoctor));
        when(doctorMapper.toResponseDTO(any(Doctor.class))).thenReturn(doctorResponseDTO);

        // Act
        List<DoctorResponseDTO> result = doctorService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Dr. Juan Pérez", result.get(0).name());
    }

    @Test
    void findById_Success() {
        // Arrange
        when(doctorRepository.findById(testCedula)).thenReturn(Optional.of(testDoctor));
        when(doctorMapper.toResponseDTO(testDoctor)).thenReturn(doctorResponseDTO);

        // Act
        DoctorResponseDTO result = doctorService.findById(testCedula);

        // Assert
        assertNotNull(result);
        assertEquals(testCedula, result.cedula());
    }

    @Test
    void findById_Fails_NotFound() {
        // Arrange
        when(doctorRepository.findById(testCedula)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.findById(testCedula);
        });
    }

    @Test
    void save_Success() {
        // Arrange
        when(doctorRepository.existsById(anyString())).thenReturn(false);
        when(doctorRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(specialityRepository.findByName("Cardiología")).thenReturn(Optional.of(testSpeciality));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);
        when(doctorMapper.toResponseDTO(testDoctor)).thenReturn(doctorResponseDTO);

        // Act
        DoctorResponseDTO result = doctorService.save(doctorRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testCedula, result.cedula());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void save_Fails_CedulaAlreadyExists() {
        // Arrange
        when(doctorRepository.existsById(testCedula)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            doctorService.save(doctorRequestDTO);
        });
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void save_Fails_EmailAlreadyExists() {
        // Arrange
        when(doctorRepository.existsById(anyString())).thenReturn(false);
        when(doctorRepository.findByEmail(anyString())).thenReturn(Optional.of(testDoctor));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            doctorService.save(doctorRequestDTO);
        });
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void save_Fails_SpecialityNotFound() {
        // Arrange
        when(doctorRepository.existsById(anyString())).thenReturn(false);
        when(doctorRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(specialityRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.save(doctorRequestDTO);
        });
        verify(doctorRepository, never()).save(any(Doctor.class));
    }
    
    @Test
    void update_Success() {
        // Arrange
        DoctorRequestDTO updateRequest = new DoctorRequestDTO(testCedula, "Dr. Juan Pérez Actualizado", "3004445566", "jperez.new@example.com", "Cardiología");
        when(doctorRepository.findById(testCedula)).thenReturn(Optional.of(testDoctor));
        when(doctorRepository.existsByEmailAndCedulaNot(updateRequest.email(), testCedula)).thenReturn(false);
        when(specialityRepository.findByName(updateRequest.specialityName())).thenReturn(Optional.of(testSpeciality));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);
        when(doctorMapper.toResponseDTO(testDoctor)).thenReturn(new DoctorResponseDTO(testCedula, "Dr. Juan Pérez Actualizado", "3004445566", "jperez.new@example.com", "Cardiología"));

        // Act
        DoctorResponseDTO result = doctorService.update(testCedula, updateRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals("Dr. Juan Pérez Actualizado", result.name());
        verify(doctorRepository, times(1)).save(testDoctor);
    }

    @Test
    void delete_Success() {
        // Arrange
        when(doctorRepository.existsById(testCedula)).thenReturn(true);

        // Act
        doctorService.delete(testCedula);

        // Assert
        verify(doctorRepository, times(1)).deleteById(testCedula);
    }

    @Test
    void delete_Fails_NotFound() {
        // Arrange
        when(doctorRepository.existsById(testCedula)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.delete(testCedula);
        });
        verify(doctorRepository, never()).deleteById(anyString());
    }
}