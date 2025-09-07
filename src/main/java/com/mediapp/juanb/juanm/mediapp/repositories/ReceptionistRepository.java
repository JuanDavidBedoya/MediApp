package com.mediapp.juanb.juanm.mediapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Receptionist;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, String> {

}
