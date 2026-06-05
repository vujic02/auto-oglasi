package com.autooglasi.service;

import com.autooglasi.entity.Feature;

import java.util.List;

public interface FeatureService {

    List<Feature> findAll();

    Feature findById(Long id);

    Feature save(Feature feature);

    void delete(Long id);
}
