package com.autooglasi.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model automobila (Serija 3, A4, Golf...).
 * ManyToOne -> {@link Brand}
 * OneToMany -> {@link Advertisement}
 */
@Entity
@Table(name = "car_models")
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "carModel")
    private List<Advertisement> advertisements = new ArrayList<>();

    public CarModel() {
    }

    public CarModel(String name, Brand brand) {
        this.name = name;
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    /** Pun naziv: "BMW Serija 3". */
    public String getFullName() {
        return (brand != null ? brand.getName() + " " : "") + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarModel carModel)) return false;
        return id != null && id.equals(carModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
