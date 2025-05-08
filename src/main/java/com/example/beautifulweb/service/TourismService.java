package com.example.beautifulweb.service;

import java.util.List;
import java.util.stream.Collectors;
import com.example.beautifulweb.model.Tourism;
import org.springframework.stereotype.Service;
import com.example.beautifulweb.repository.TourismRepository;

@Service
public class TourismService {

    private final TourismRepository repository;

    public TourismService(TourismRepository repository) {
        this.repository = repository;
    }

    public List<Tourism> getAll() {
        return repository.findAll();
    }
    public void save(Tourism tourism) {
        repository.save(tourism);
    } 

    public Tourism getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    
}
