package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Eps;

@Repository
public interface EpsRepository extends JpaRepository<Eps, UUID> {

    Optional<Eps> findByName(String name);

}
