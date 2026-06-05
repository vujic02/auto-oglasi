package com.autooglasi.service.impl;

import com.autooglasi.entity.Brand;
import com.autooglasi.entity.CarModel;
import com.autooglasi.exception.ResourceNotFoundException;
import com.autooglasi.repository.BrandRepository;
import com.autooglasi.repository.CarModelRepository;
import com.autooglasi.service.CarModelService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;
    private final BrandRepository brandRepository;

    public CarModelServiceImpl(CarModelRepository carModelRepository, BrandRepository brandRepository) {
        this.carModelRepository = carModelRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarModel> findAll() {
        return carModelRepository.findAll(Sort.by("brand.name", "name"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarModel> findByBrand(Long brandId) {
        return carModelRepository.findByBrandIdOrderByName(brandId);
    }

    @Override
    @Transactional(readOnly = true)
    public CarModel findById(Long id) {
        return carModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Model nije pronađen: " + id));
    }

    @Override
    public CarModel save(CarModel model, Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Marka nije pronađena: " + brandId));
        model.setBrand(brand);
        return carModelRepository.save(model);
    }

    @Override
    public void delete(Long id) {
        if (!carModelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Model nije pronađen: " + id);
        }
        carModelRepository.deleteById(id);
    }
}
