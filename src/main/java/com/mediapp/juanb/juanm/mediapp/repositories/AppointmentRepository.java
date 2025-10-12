package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findConflictingAppointment(
        @Param("doctorCedula") String doctorCedula,
        @Param("patientCedula") String patientCedula,
        @Param("date") Date date,
        @Param("time") Date time);
}
