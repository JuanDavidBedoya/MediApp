package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Appointment;
import com.mediapp.juanb.juanm.mediapp.repositories.AppointmentRepository;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findById(UUID uuid) {
        return appointmentRepository.findById(uuid);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void delete(UUID uuid) {
        appointmentRepository.deleteById(uuid);
    }

    public Appointment update(UUID uuid, Appointment appointment) {
        appointment.setIdAppointment(uuid);
        return appointmentRepository.save(appointment);
    }
}