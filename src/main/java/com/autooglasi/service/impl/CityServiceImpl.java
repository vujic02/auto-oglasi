package com.autooglasi.service.impl;

import com.autooglasi.entity.City;
import com.autooglasi.exception.ResourceNotFoundException;
import com.autooglasi.repository.CityRepository;
import com.autooglasi.service.CityService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> findAll() {
        return cityRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional(readOnly = true)
    public City findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grad nije pronađen: " + id));
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public void delete(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grad nije pronađen: " + id);
        }
        cityRepository.deleteById(id);
    }
}
