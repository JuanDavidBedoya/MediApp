package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<com.mediapp.juanb.juanm.mediapp.entities.Speciality, UUID> {

}
