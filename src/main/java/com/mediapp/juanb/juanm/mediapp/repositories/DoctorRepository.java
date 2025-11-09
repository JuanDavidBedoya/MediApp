package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository <Doctor, String>{

    Optional<Doctor> findByEmail(String email);

    @Query("SELECT COUNT(d) > 0 FROM Doctor d WHERE d.email = :email AND d.cedula != :cedula")
    boolean existsByEmailAndCedulaNot(@Param("email") String email, @Param("cedula") String cedula);

    Optional<Doctor> findByCedula(String cedula);

    @Query("SELECT d FROM Doctor d WHERE d.speciality.name = :specialityName ORDER BY d.cedula LIMIT 1")
    Optional<Doctor> findFirstBySpecialityName(@Param("specialityName") String specialityName);
}


