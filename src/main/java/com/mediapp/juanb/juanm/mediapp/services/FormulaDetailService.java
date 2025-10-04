package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.FormulaDetail;
import com.mediapp.juanb.juanm.mediapp.repositories.FormulaDetailRepository;

@Service
public class FormulaDetailService {

    private FormulaDetailRepository formulaDetailRepository;

    public FormulaDetailService(FormulaDetailRepository formulaDetailRepository) {
        this.formulaDetailRepository = formulaDetailRepository;
    }

    public List<FormulaDetail> findAll() {
        return formulaDetailRepository.findAll();
    }

    public Optional<FormulaDetail> findById(UUID uuid) {
        return formulaDetailRepository.findById(uuid);
    }

    public FormulaDetail save(FormulaDetail formulaDetail) {
        return formulaDetailRepository.save(formulaDetail);
    }

    public void delete(UUID uuid) {
        formulaDetailRepository.deleteById(uuid);
    }

    public FormulaDetail update(UUID uuid, FormulaDetail formulaDetail) {
        formulaDetail.setIdFormulaDetail(uuid);
        return formulaDetailRepository.save(formulaDetail);
    }
}