package com.autooglasi.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Oprema/karakteristika vozila (ABS, Klima, Navigacija, Koža...).
 * ManyToMany sa {@link Advertisement}.
 */
@Entity
@Table(name = "features")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @ManyToMany(mappedBy = "features")
    private Set<Advertisement> advertisements = new HashSet<>();

    public Feature() {
    }

    public Feature(String name) {
        this.name = name;
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

    public Set<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(Set<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feature feature)) return false;
        return id != null && id.equals(feature.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
