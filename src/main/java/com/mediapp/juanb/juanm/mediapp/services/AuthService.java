package com.mediapp.juanb.juanm.mediapp.services;

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
import jakarta.transaction.Transactional;
import com.mediapp.juanb.juanm.mediapp.dtos.AuthResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.LoginRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EpsRepository epsRepository;
    private final PhoneRepository phoneRepository;
    private final UserPhoneRepository userPhoneRepository;
    private final CityRepository cityRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       EpsRepository epsRepository,
                       PhoneRepository phoneRepository,
                       UserPhoneRepository userPhoneRepository,
                       CityRepository cityRepository) { 
                        
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.epsRepository = epsRepository;
        this.phoneRepository = phoneRepository;
        this.userPhoneRepository = userPhoneRepository;
        this.cityRepository = cityRepository;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.cedula(), request.contrasena())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByCedula(userDetails.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con cédula: " + userDetails.getUsername()));
        String jwtToken = jwtService.generateToken(userDetails);
        UserResponseDTO userDTO = userMapper.toUserResponseDTO(user);

        return new AuthResponseDTO(jwtToken, userDTO);
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
