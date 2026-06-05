package com.autooglasi.repository;

import com.autooglasi.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    boolean existsByNameIgnoreCase(String name);
}
