package com.autooglasi.service;

import com.autooglasi.dto.AdvertisementForm;
import com.autooglasi.entity.Advertisement;
import com.autooglasi.entity.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface AdvertisementService {

    Page<Advertisement> search(String q, Long brandId, FuelType fuelType,
                               BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Advertisement findById(Long id);

    List<Advertisement> findBySeller(Long sellerId);

    Advertisement create(AdvertisementForm form, String username);

    Advertisement update(Long id, AdvertisementForm form, String username);

    void delete(Long id, String username);

    /** Da li dati korisnik sme da menja/briše oglas (vlasnik ili admin). */
    boolean canModify(Advertisement ad, String username, boolean isAdmin);
}
