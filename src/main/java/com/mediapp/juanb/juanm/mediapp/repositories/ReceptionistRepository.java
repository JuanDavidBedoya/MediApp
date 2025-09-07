package com.mediapp.juanb.juanm.mediapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends JpaRepository<com.mediapp.juanb.juanm.mediapp.entities.Receptionist, String> {

}
