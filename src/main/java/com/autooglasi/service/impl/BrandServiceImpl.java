package com.autooglasi.service.impl;

import com.autooglasi.entity.Brand;
import com.autooglasi.exception.ResourceNotFoundException;
import com.autooglasi.repository.BrandRepository;
import com.autooglasi.service.BrandService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        return brandRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional(readOnly = true)
    public Brand findById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marka nije pronađena: " + id));
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public void delete(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Marka nije pronađena: " + id);
        }
        brandRepository.deleteById(id);
    }
}
