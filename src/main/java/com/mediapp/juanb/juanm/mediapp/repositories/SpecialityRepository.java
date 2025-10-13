package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Speciality;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, UUID> {

    Optional<Speciality> findByName(String name);

}
