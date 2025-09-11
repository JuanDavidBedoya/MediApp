package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Bill;
import com.mediapp.juanb.juanm.mediapp.repositories.BillRepository;

@Service
public class BillService {

    private BillRepository billRepository;

    public BillService(BillRepository billRepository){
        this.billRepository = billRepository;
    }

    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    public Bill update(UUID id, Bill bill) {
        bill.setUuidBill(id);
        return billRepository.save(bill);
    }

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public Optional <Bill> findById(UUID id) {
        return billRepository.findById(id);
    
    }   
    
    public void delete(UUID id) {
        billRepository.deleteById(id);
    }

}
