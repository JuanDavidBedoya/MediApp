package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;

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

    public Optional<Appointment> findById(java.util.UUID uuid) {
        return appointmentRepository.findById(uuid);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void delete(java.util.UUID uuid) {
        appointmentRepository.deleteById(uuid);
    }

    public Appointment update(java.util.UUID uuid, Appointment appointment) {
        appointment.setUuid(uuid);
        return appointmentRepository.save(appointment);
    }
}
