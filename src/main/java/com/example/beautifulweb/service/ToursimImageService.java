package com.example.beautifulweb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.repository.ToursimImageRepository;

@Service
public class ToursimImageService {
    private final ToursimImageRepository tourismImageRepository;

    public ToursimImageService(ToursimImageRepository tourismImageRepository) {
        this.tourismImageRepository = tourismImageRepository;
    }
    public void saveTourismImage(TourismImage tourismImage) {
        tourismImageRepository.save(tourismImage);
    }

    public List<TourismImage> getAllImages() {
        return tourismImageRepository.findAll();
    }

    public List<TourismImage> getImagesByTourismId(Long tourismId) {
        return tourismImageRepository.findByTourismId(tourismId);
    }
}
