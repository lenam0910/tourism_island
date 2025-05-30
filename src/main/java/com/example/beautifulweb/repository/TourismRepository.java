package com.example.beautifulweb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.beautifulweb.model.Tourism;
@Repository
public interface TourismRepository extends JpaRepository<Tourism, Long> {
    // This interface extends JpaRepository to provide CRUD operations for the TourismService entity.
    // It allows us to perform database operations without writing boilerplate code.
    // We can add custom query methods here if needed.
    void deleteById(Long id);
}
