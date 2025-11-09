package com.mediapp.juanb.juanm.mediapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mediapp.juanb.juanm.mediapp.dtos.AppointmentResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.FormulaDetailResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserResponseDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.UserUpdateDTO;
import com.mediapp.juanb.juanm.mediapp.services.AppointmentService;
import com.mediapp.juanb.juanm.mediapp.services.FormulaDetailService;
import com.mediapp.juanb.juanm.mediapp.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AppointmentService appointmentService;
    private final FormulaDetailService formulaDetailService;

    public UserController(UserService userService, AppointmentService appointmentService, FormulaDetailService formulaDetailService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.formulaDetailService = formulaDetailService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/eps/{epsName}")
    public ResponseEntity<List<UserResponseDTO>> getByEpsName(@PathVariable("epsName") String epsName) {
        return ResponseEntity.ok(userService.findByEpsName(epsName));
    }

    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<UserResponseDTO>> getByCityName(@PathVariable("cityName") String cityName) {
        return ResponseEntity.ok(userService.findByCityName(cityName));
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<UserResponseDTO> getByCedula(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(userService.findByCedula(cedula));
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable("cedula") String cedula, @Valid @RequestBody UserUpdateDTO userDTO) {
        UserResponseDTO updatedUser = userService.update(cedula, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{cedula}/appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByCedula(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(appointmentService.findByPatientCedula(cedula));
    }

    @GetMapping("/{cedula}/formula-details")
    public ResponseEntity<List<FormulaDetailResponseDTO>> getFormulaDetailsByCedula(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(formulaDetailService.findByPatientCedula(cedula));
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        userService.delete(cedula);
        return ResponseEntity.noContent().build();
    }
}