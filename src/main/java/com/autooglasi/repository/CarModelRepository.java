package com.autooglasi.repository;

import com.autooglasi.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    List<CarModel> findByBrandIdOrderByName(Long brandId);
}
