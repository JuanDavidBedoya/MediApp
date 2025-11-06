package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.dtos.DoctorRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.DoctorResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.DoctorUpdateDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.DoctorMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.DoctorRepository;
import com.mediapp.juanb.juanm.mediapp.repositories.SpecialityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialityRepository specialityRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, SpecialityRepository specialityRepository, DoctorMapper doctorMapper, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.specialityRepository = specialityRepository;
        this.doctorMapper = doctorMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<DoctorResponseDTO> findAll() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DoctorResponseDTO findById(String cedula) {
        Doctor doctor = doctorRepository.findById(cedula)

                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con cédula: " + cedula));
        return doctorMapper.toResponseDTO(doctor);
    }

    public DoctorResponseDTO save(DoctorRequestDTO doctorDTO) {
        if (doctorRepository.existsById(doctorDTO.cedula())) {

            throw new ResourceAlreadyExistsException("Ya existe un doctor con la cédula: " + doctorDTO.cedula());
        }
        if (doctorRepository.findByEmail(doctorDTO.email()).isPresent()) {

            throw new ResourceAlreadyExistsException("Ya existe un doctor con el correo: " + doctorDTO.email());
        }

        Speciality speciality = specialityRepository.findByName(doctorDTO.specialityName())
 
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con nombre: " + doctorDTO.specialityName()));

        Doctor newDoctor = new Doctor();
        newDoctor.setCedulaDoctor(doctorDTO.cedula());
        newDoctor.setName(doctorDTO.name());
        newDoctor.setPhone(doctorDTO.phone());
        newDoctor.setEmail(doctorDTO.email());
        newDoctor.setSpeciality(speciality);

        Doctor savedDoctor = doctorRepository.save(newDoctor);
        return doctorMapper.toResponseDTO(savedDoctor);
    }

    public DoctorResponseDTO update(String cedula, DoctorUpdateDTO doctorDTO) {
        Doctor existingDoctor = doctorRepository.findById(cedula)

                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con cédula: " + cedula));

        if (doctorRepository.existsByEmailAndCedulaNot(doctorDTO.email(), cedula)) {

            throw new ResourceAlreadyExistsException("El nuevo correo ya está en uso por otro doctor.");
        }

        Speciality speciality = specialityRepository.findByName(doctorDTO.specialityName())

                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con nombre: " + doctorDTO.specialityName()));

        existingDoctor.setName(doctorDTO.name());
        existingDoctor.setPhone(doctorDTO.phone());
        existingDoctor.setEmail(doctorDTO.email());
        existingDoctor.setSpeciality(speciality);

        if (doctorDTO.password() != null && !doctorDTO.password().isBlank()) {
            existingDoctor.setPassword(passwordEncoder.encode(doctorDTO.password()));
        }

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return doctorMapper.toResponseDTO(updatedDoctor);
    }

    public void delete(String cedula) {
        if (!doctorRepository.existsById(cedula)) {

            throw new ResourceNotFoundException("Doctor no encontrado con cédula: " + cedula);
        }
        doctorRepository.deleteById(cedula);
    }
}