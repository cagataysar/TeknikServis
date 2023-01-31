package com.garanti.TeknikServis.service;

import com.garanti.TeknikServis.model.Sale;
import com.garanti.TeknikServis.repo.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SaleService {

    private SaleRepository repository;

    public List< Sale > getAll() {
        return repository.getAll();
    }

    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }

    public boolean save ( Sale sale)
    {
        try
        {
            return repository.save(sale);
        }
        catch (Exception e) {
            return false;
        }
    }
}
