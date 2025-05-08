package com.example.beautifulweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.beautifulweb.model.TourBooking;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBooking, Long> {
}