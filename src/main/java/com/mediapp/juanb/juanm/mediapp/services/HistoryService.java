package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.History;
import com.mediapp.juanb.juanm.mediapp.repositories.HistoryRepository;

@Service
public class HistoryService {

    private HistoryRepository historiesRepository;

    public HistoryService(HistoryRepository historiesRepository){
        this.historiesRepository = historiesRepository;
    }

    public History save(History histories) {
        return historiesRepository.save(histories);
    }

    public History update(UUID id, History histories) {
        histories.setIdHistory(id);
        return historiesRepository.save(histories);
    }

    public List<History> findAll() {
        return historiesRepository.findAll();
    }

    public Optional <History> findById(UUID id) {
        return historiesRepository.findById(id);
    
    }   
    
    public void delete(UUID id) {
        historiesRepository.deleteById(id);
    }

}
