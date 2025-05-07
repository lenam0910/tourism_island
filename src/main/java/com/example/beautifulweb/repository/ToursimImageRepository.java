package com.example.beautifulweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.beautifulweb.model.TourismImage;

@Repository
public interface ToursimImageRepository extends JpaRepository<TourismImage, Long> {
    // Các phương thức truy vấn tùy chỉnh nếu cần
    // Ví dụ: tìm kiếm theo đường dẫn ảnh hoặc theo đối tượng Tourism
    List<TourismImage> findByTourismId(Long tourismId); // Tìm ảnh theo id của đối tượng Tourism
}
