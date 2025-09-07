package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Histories;
import com.mediapp.juanb.juanm.mediapp.repositories.HistoriesRepository;

@Service
public class HistoriesService {

    private HistoriesRepository historiesRepository;

    public HistoriesService(HistoriesRepository historiesRepository){
        this.historiesRepository = historiesRepository;
    }

    public Histories save(Histories histories) {
        return historiesRepository.save(histories);
    }

    public Histories update(UUID id, Histories histories) {
        histories.setIdHistory(id);
        return historiesRepository.save(histories);
    }

    public List<Histories> findAll() {
        return historiesRepository.findAll();
    }

    public Optional <Histories> findById(UUID id) {
        return historiesRepository.findById(id);
    
    }   
    
    public void delete(UUID id) {
        historiesRepository.deleteById(id);
    }

}
