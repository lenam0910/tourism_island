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

    public List<Tourism> findNearby(double lat, double lng, double radiusMeters) {
        return repository.findAll().stream()
                .filter(s -> haversine(lat, lng, s.getLatitude(), s.getLongitude()) <= radiusMeters)
                .collect(Collectors.toList());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return 2 * R * Math.asin(Math.sqrt(a));
    }

    public List<Tourism> getAll() {
        return repository.findAll();
    }
    public void save(Tourism tourism) {
        repository.save(tourism);
    }

}
