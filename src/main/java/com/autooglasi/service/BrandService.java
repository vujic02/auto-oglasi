package com.autooglasi.service;

import com.autooglasi.entity.Brand;

import java.util.List;

public interface BrandService {

    List<Brand> findAll();

    Brand findById(Long id);

    Brand save(Brand brand);

    void delete(Long id);
}
