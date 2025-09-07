package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Histories;

@Repository
public interface HistoriesRepository extends JpaRepository <Histories, UUID>{

}
