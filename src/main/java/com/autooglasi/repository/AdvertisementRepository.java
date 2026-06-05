package com.autooglasi.repository;

import com.autooglasi.entity.Advertisement;
import com.autooglasi.entity.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    /**
     * Pretraga/filtriranje oglasa. Svi parametri su opcioni (null = nije primenjen filter).
     */
    @Query("""
            SELECT a FROM Advertisement a
            WHERE (:q IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:brandId IS NULL OR a.carModel.brand.id = :brandId)
              AND (:fuelType IS NULL OR a.fuelType = :fuelType)
              AND (:minPrice IS NULL OR a.price >= :minPrice)
              AND (:maxPrice IS NULL OR a.price <= :maxPrice)
            ORDER BY a.createdAt DESC
            """)
    Page<Advertisement> search(@Param("q") String q,
                               @Param("brandId") Long brandId,
                               @Param("fuelType") FuelType fuelType,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               Pageable pageable);
}
