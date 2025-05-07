package com.example.beautifulweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "tourism_image")
public class TourismImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagePath;
    
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "tourism_id", nullable = false)
    private Tourism tourism; // Tham chiếu đến đối tượng Tourism tương ứng
}
