package com.mediapp.juanb.juanm.mediapp.services;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EpsRepository epsRepository;
    private final CityRepository cityRepository; // Se necesita para la lógica de actualización
    private final PasswordEncoder passwordEncoder;
    private final UserPhoneRepository userPhoneRepository;
    private final PhoneRepository phoneRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, EpsRepository epsRepository, 
        CityRepository cityRepository, PasswordEncoder passwordEncoder, UserPhoneRepository userPhoneRepository, PhoneRepository phoneRepository) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.epsRepository = epsRepository;
        this.cityRepository = cityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userPhoneRepository = userPhoneRepository;
        this.phoneRepository = phoneRepository;
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findByCedula(String cedula) {
        User user = userRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula));
        return userMapper.toUserResponseDTO(user);
    }

    public void delete(String cedula) {
        if (!userRepository.existsById(cedula)) {
            throw new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula);
        }
        userRepository.deleteById(cedula);
    }

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        // Este método debe lanzar UsernameNotFoundException para la integración correcta con Spring Security
        return userRepository.findByCedula(cedula)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con cédula: " + cedula));
    }

    public UserResponseDTO save(UserRequestDTO userDTO) {
        if (userRepository.existsById(userDTO.cedula())) {
            throw new ResourceAlreadyExistsException("La cédula ya está registrada.");
        }
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new ResourceAlreadyExistsException("El correo ya está registrado.");
        }

        Eps eps = epsRepository.findByName(userDTO.epsName())
                .orElseThrow(() -> new ResourceNotFoundException("EPS no encontrada con nombre: " + userDTO.epsName()));
        
        City city = cityRepository.findByName(userDTO.cityName())
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada con nombre: " + userDTO.cityName()));

        User newUser = new User();
        newUser.setCedula(userDTO.cedula());
        newUser.setName(userDTO.name());
        newUser.setEmail(userDTO.email());
        newUser.setPassword(passwordEncoder.encode(userDTO.contrasena()));
        newUser.setEps(eps);
        newUser.setCity(city);

        User savedUser = userRepository.save(newUser);
        return userMapper.toUserResponseDTO(savedUser);
    }

    public UserResponseDTO update(String cedula, UserUpdateDTO userDTO) {
        User existingUser = userRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula));

        if (userRepository.existsByEmailAndCedulaNot(userDTO.email(), cedula)) {
            throw new ResourceAlreadyExistsException("El nuevo correo ya está en uso por otro usuario.");
        }

        Eps eps = epsRepository.findByName(userDTO.epsName())
                .orElseThrow(() -> new ResourceNotFoundException("EPS no encontrada con nombre: " + userDTO.epsName()));
        
        City city = cityRepository.findByName(userDTO.cityName())
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada con nombre: " + userDTO.cityName()));

        existingUser.setName(userDTO.name());
        existingUser.setEmail(userDTO.email());
        existingUser.setEps(eps);
        existingUser.setCity(city);

        if (userDTO.contrasena() != null && !userDTO.contrasena().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.contrasena()));
        }

        if (userDTO.phones() != null) {

            userPhoneRepository.deleteAll(existingUser.getUserPhones());

            existingUser.getUserPhones().clear();
            

            for (String phoneNumber : userDTO.phones()) {
                Phone phone = phoneRepository.findByPhone(phoneNumber)
                        .orElseGet(() -> phoneRepository.save(new Phone(phoneNumber)));
                UserPhone userPhone = new UserPhone(existingUser, phone);
                userPhoneRepository.save(userPhone);
            }
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDTO(updatedUser);
    }
}