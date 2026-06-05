package com.autooglasi.service.impl;

import com.autooglasi.entity.Feature;
import com.autooglasi.exception.ResourceNotFoundException;
import com.autooglasi.repository.FeatureRepository;
import com.autooglasi.service.FeatureService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feature> findAll() {
        return featureRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional(readOnly = true)
    public Feature findById(Long id) {
        return featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oprema nije pronađena: " + id));
    }

    @Override
    public Feature save(Feature feature) {
        return featureRepository.save(feature);
    }

    @Override
    public void delete(Long id) {
        if (!featureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Oprema nije pronađena: " + id);
        }
        featureRepository.deleteById(id);
    }
}
