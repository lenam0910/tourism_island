package com.example.beautifulweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "package")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String packageDetail;
    
    @ManyToOne
    @JoinColumn(name = "tourism_id", nullable = false)
    private Tourism tourism;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageDetail() {
        return packageDetail;
    }

    public void setPackageDetail(String packageDetail) {
        this.packageDetail = packageDetail;
    }

    public Tourism getTourism() {
        return tourism;
    }

    public void setTourism(Tourism tourism) {
        this.tourism = tourism;
    }

    
}
