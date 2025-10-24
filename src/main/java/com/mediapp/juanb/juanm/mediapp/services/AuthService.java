package com.mediapp.juanb.juanm.mediapp.services;

import com.mediapp.juanb.juanm.mediapp.dtos.AuthResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.LoginRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.DoctorRequestDTO; 
import com.mediapp.juanb.juanm.mediapp.dtos.DoctorResponseDTO; 
import com.mediapp.juanb.juanm.mediapp.entities.*;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceAlreadyExistsException;
import com.mediapp.juanb.juanm.mediapp.exceptions.ResourceNotFoundException;
import com.mediapp.juanb.juanm.mediapp.mappers.DoctorMapper; 
import com.mediapp.juanb.juanm.mediapp.mappers.UserMapper;
import com.mediapp.juanb.juanm.mediapp.repositories.*; 
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository; 
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final DoctorMapper doctorMapper; 
    private final EpsRepository epsRepository;
    private final PhoneRepository phoneRepository;
    private final UserPhoneRepository userPhoneRepository;
    private final CityRepository cityRepository;
    private final SpecialityRepository specialityRepository; 

    // --- CONSTRUCTOR ACTUALIZADO ---
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       DoctorRepository doctorRepository, 
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       DoctorMapper doctorMapper, 
                       EpsRepository epsRepository,
                       PhoneRepository phoneRepository,
                       UserPhoneRepository userPhoneRepository,
                       CityRepository cityRepository,
                       SpecialityRepository specialityRepository) { 

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository; 
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.doctorMapper = doctorMapper;
        this.epsRepository = epsRepository;
        this.phoneRepository = phoneRepository;
        this.userPhoneRepository = userPhoneRepository;
        this.cityRepository = cityRepository;
        this.specialityRepository = specialityRepository; 
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.cedula(), request.password())
        );

        var userOptional = userRepository.findByCedula(request.cedula());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getCedula())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();

            String jwtToken = jwtService.generateToken(userDetails);
            UserResponseDTO userDto = userMapper.toUserResponseDTO(user);
            return new AuthResponseDTO(jwtToken, userDto, "USER");
        }

        var doctorOptional = doctorRepository.findByCedula(request.cedula());
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(doctor.getCedulaDoctor()) 
                    .password(doctor.getPassword())
                    .roles("DOCTOR")
                    .build();

            String jwtToken = jwtService.generateToken(userDetails);
            DoctorResponseDTO doctorDto = doctorMapper.toResponseDTO(doctor); 
            return new AuthResponseDTO(jwtToken, doctorDto, "DOCTOR");
        }

        throw new UsernameNotFoundException("Usuario no encontrado con cédula: " + request.cedula());
    }

    @Transactional
    public DoctorResponseDTO registerDoctor(DoctorRequestDTO request) {
        if (doctorRepository.findByCedula(request.cedula()).isPresent() || userRepository.findByCedula(request.cedula()).isPresent()) {
            throw new ResourceAlreadyExistsException("La cédula '" + request.cedula() + "' ya está registrada.");
        }
        if (doctorRepository.findByEmail(request.email()).isPresent() || userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResourceAlreadyExistsException("El correo '" + request.email() + "' ya está registrado.");
        }

        Speciality speciality = specialityRepository.findByName(request.specialityName())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con ID: " + request.specialityName()));

        Doctor newDoctor = new Doctor();
        newDoctor.setCedula(request.cedula());
        newDoctor.setName(request.name());
        newDoctor.setEmail(request.email());
        newDoctor.setPassword(passwordEncoder.encode(request.password()));
        newDoctor.setPhone(request.phone());
        newDoctor.setSpeciality(speciality);

        Doctor savedDoctor = doctorRepository.save(newDoctor);
        return doctorMapper.toResponseDTO(savedDoctor);
    }

    @Transactional
    public UserResponseDTO register(UserRequestDTO request) {
        if (userRepository.findByCedula(request.cedula()).isPresent()) {
            throw new ResourceAlreadyExistsException("La cédula '" + request.cedula() + "' ya está registrada.");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResourceAlreadyExistsException("El correo '" + request.email() + "' ya está registrado.");
        }

        City city = cityRepository.findByName(request.cityName())
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada con nombre: " + request.cityName()));

        Eps eps = epsRepository.findByName(request.epsName())
                .orElseThrow(() -> new ResourceNotFoundException("EPS no encontrada con nombre: " + request.epsName()));

        User newUser = new User();
        newUser.setCedula(request.cedula());
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.contrasena()));
        newUser.setEps(eps);
        newUser.setCity(city);

        User savedUser = userRepository.save(newUser);

        if (request.phones() != null && !request.phones().isEmpty()) {
            for (String phoneNumber : request.phones()) {
                Phone phone = phoneRepository.findByPhone(phoneNumber)
                        .orElseGet(() -> phoneRepository.save(new Phone(phoneNumber)));
                UserPhone userPhone = new UserPhone(savedUser, phone);
                userPhoneRepository.save(userPhone);
            }
        }

        User userWithRelations = userRepository.findById(savedUser.getCedula()).get();
        return userMapper.toUserResponseDTO(userWithRelations);
    }
}