package com.autooglasi.service;

import com.autooglasi.entity.City;

import java.util.List;

public interface CityService {

    List<City> findAll();

    City findById(Long id);

    City save(City city);

    void delete(Long id);
}
