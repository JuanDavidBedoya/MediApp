package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {

}
