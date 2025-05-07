package com.example.beautifulweb.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "tourism")
public class Tourism {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private double price;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "tourism")
    @JsonIgnore
    private Set<TourismImage> images = new HashSet<>(); // Danh sách ảnh liên quan đến đối tượng Tourism này
    
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "tourism")
    @JsonIgnore
    private Set<Package> packages = new HashSet<>(); // Danh sách gói liên quan đến đối tượng Tourism này
    
}
