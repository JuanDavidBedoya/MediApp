package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Formula;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaRepository;

@Service
public class FormulaService {

    private FormulaRepository formulaRepository;

    public FormulaService(FormulaRepository formulaRepository) {
        this.formulaRepository = formulaRepository;
    }

    public List<Formula> findAll() {
        return formulaRepository.findAll();
    }

    public Optional<Formula> findById(UUID uuid) {
        return formulaRepository.findById(uuid);
    }

    public Formula save(Formula formula) {
        return formulaRepository.save(formula);
    }

    public void delete(UUID uuid) {
        formulaRepository.deleteById(uuid);
    }

    public Formula update(UUID uuid, Formula formula) {
        formula.setIdFormula(uuid);
        return formulaRepository.save(formula);
    }
}