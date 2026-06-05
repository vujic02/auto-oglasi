package com.autooglasi.service.impl;

import com.autooglasi.dto.AdvertisementForm;
import com.autooglasi.entity.*;
import com.autooglasi.exception.ResourceNotFoundException;
import com.autooglasi.repository.*;
import com.autooglasi.service.AdvertisementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final CarModelRepository carModelRepository;
    private final CityRepository cityRepository;
    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;

    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository,
                                    CarModelRepository carModelRepository,
                                    CityRepository cityRepository,
                                    FeatureRepository featureRepository,
                                    UserRepository userRepository) {
        this.advertisementRepository = advertisementRepository;
        this.carModelRepository = carModelRepository;
        this.cityRepository = cityRepository;
        this.featureRepository = featureRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Advertisement> search(String q, Long brandId, FuelType fuelType,
                                      BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        String query = (q != null && !q.isBlank()) ? q.trim() : null;
        return advertisementRepository.search(query, brandId, fuelType, minPrice, maxPrice, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oglas nije pronađen: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Advertisement> findBySeller(Long sellerId) {
        return advertisementRepository.findBySellerIdOrderByCreatedAtDesc(sellerId);
    }

    @Override
    public Advertisement create(AdvertisementForm form, String username) {
        User seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik nije pronađen: " + username));

        Advertisement ad = new Advertisement();
        ad.setSeller(seller);
        applyForm(ad, form);
        return advertisementRepository.save(ad);
    }

    @Override
    public Advertisement update(Long id, AdvertisementForm form, String username) {
        Advertisement ad = findById(id);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik nije pronađen: " + username));

        if (!canModify(ad, username, hasAdminRole(user))) {
            throw new AccessDeniedException("Nemate pravo da menjate ovaj oglas");
        }
        applyForm(ad, form);
        return advertisementRepository.save(ad);
    }

    @Override
    public void delete(Long id, String username) {
        Advertisement ad = findById(id);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik nije pronađen: " + username));

        if (!canModify(ad, username, hasAdminRole(user))) {
            throw new AccessDeniedException("Nemate pravo da obrišete ovaj oglas");
        }
        advertisementRepository.delete(ad);
    }

    @Override
    public boolean canModify(Advertisement ad, String username, boolean isAdmin) {
        return isAdmin || (ad.getSeller() != null && ad.getSeller().getUsername().equals(username));
    }

    // ===== Pomocne metode =====

    private boolean hasAdminRole(User user) {
        return user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
    }

    /** Prepisuje vrednosti iz forme u entitet (ukey relacije: model, grad, oprema). */
    private void applyForm(Advertisement ad, AdvertisementForm form) {
        ad.setTitle(form.getTitle());
        ad.setDescription(form.getDescription());
        ad.setPrice(form.getPrice());
        ad.setYear(form.getYear());
        ad.setMileage(form.getMileage());
        ad.setFuelType(form.getFuelType());
        ad.setTransmission(form.getTransmission());
        ad.setColor(form.getColor());
        ad.setEnginePower(form.getEnginePower());
        ad.setImageUrl(form.getImageUrl());
        ad.setStatus(form.getStatus());

        CarModel model = carModelRepository.findById(form.getCarModelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model nije pronađen: " + form.getCarModelId()));
        ad.setCarModel(model);

        City city = cityRepository.findById(form.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("Grad nije pronađen: " + form.getCityId()));
        ad.setCity(city);

        Set<Feature> features = new HashSet<>();
        if (form.getFeatureIds() != null) {
            for (Long fid : form.getFeatureIds()) {
                featureRepository.findById(fid).ifPresent(features::add);
            }
        }
        ad.setFeatures(features);
    }
}
