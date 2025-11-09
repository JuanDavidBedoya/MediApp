package com.mediapp.juanb.juanm.mediapp.repositories;

import java.sql.Time;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByPatientCedula(String cedula);

    @Query("SELECT a FROM Appointment a " +
            "WHERE (a.doctor.cedula = :doctorCedula OR a.patient.cedula = :patientCedula) " +
            "AND a.date = :date AND a.time = :time")
    Optional<Appointment> findConflictingAppointment(
        @Param("doctorCedula") String doctorCedula,
        @Param("patientCedula") String patientCedula,
        @Param("date") Date date,
        @Param("time") Time time);

    @Query("SELECT a FROM Appointment a " +
            "WHERE (a.doctor.speciality.name = :specialityName OR a.patient.cedula = :patientCedula) " +
            "AND a.date = :date AND a.time = :time")
    Optional<Appointment> findConflictingAppointmentBySpeciality(
        @Param("specialityName") String specialityName,
        @Param("patientCedula") String patientCedula,
        @Param("date") Date date,
        @Param("time") Time time);
}
