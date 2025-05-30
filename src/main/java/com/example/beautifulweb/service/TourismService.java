package com.example.beautifulweb.service;

import java.util.List;
import java.util.stream.Collectors;
import com.example.beautifulweb.model.Tourism;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.beautifulweb.repository.TourismRepository;

@Service
public class TourismService {

    private final TourismRepository repository;

    public TourismService(TourismRepository repository) {
        this.repository = repository;
    }

    public Page<Tourism> findAll(Pageable pageable) {
        return repository.findAll(pageable);
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

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<String> getAllDestinations() {
        List<String> destinations = repository.findAll().stream()
                .map(Tourism::getName)
                .filter(name -> name != null && !name.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Destinations in service: " + destinations); // Log để debug
        return destinations;
    }

}
