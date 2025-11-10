package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;

@Repository
public interface FormulaDetailRepository extends JpaRepository <FormulaDetail, UUID>{
    Optional<FormulaDetail> findByFormulaIdFormulaAndMedicationIdMedication(UUID formulaId, UUID medicationId);
    Optional<FormulaDetail> findByFormulaIdFormulaAndMedicationName(UUID formulaId, String name);
    List<FormulaDetail> findByFormulaAppointmentPatientCedula(String cedula);
}
