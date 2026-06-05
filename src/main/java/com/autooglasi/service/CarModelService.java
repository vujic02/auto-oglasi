package com.autooglasi.service;

import com.autooglasi.entity.CarModel;

import java.util.List;

public interface CarModelService {

    List<CarModel> findAll();

    List<CarModel> findByBrand(Long brandId);

    CarModel findById(Long id);

    CarModel save(CarModel model, Long brandId);

    void delete(Long id);
}
