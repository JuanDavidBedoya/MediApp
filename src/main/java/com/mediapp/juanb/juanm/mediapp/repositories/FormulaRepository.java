package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mediapp.juanb.juanm.mediapp.entities.Formula;

@Repository
public interface FormulaRepository extends JpaRepository<Formula, UUID>{
    Optional<Formula> findByAppointmentIdAppointment(UUID idAppointment);

    @Query("SELECT f FROM Formula f LEFT JOIN FETCH f.formulaDetails fd LEFT JOIN FETCH fd.medication WHERE f.idFormula = :id")
    Optional<Formula> findByIdWithDetails(@Param("id") UUID id);
}
