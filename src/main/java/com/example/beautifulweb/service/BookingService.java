package com.example.beautifulweb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.beautifulweb.model.TourBooking;
import com.example.beautifulweb.repository.TourBookingRepository;

@Service
public class BookingService {
    private final TourBookingRepository tourBookingRepository;

    public BookingService(TourBookingRepository tourBookingRepository) {
        this.tourBookingRepository = tourBookingRepository;
    }

    public void saveBooking(TourBooking tourBooking) {
        tourBookingRepository.save(tourBooking);
    }

    public List<TourBooking> getAllBookings() {
        return tourBookingRepository.findAll();
    }

    public TourBooking getBookingById(Long id) {
        return tourBookingRepository.findById(id).orElse(null);
    }

    public void deleteBooking(Long id) {
        tourBookingRepository.deleteById(id);

    }
}
