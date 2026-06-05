package com.autooglasi.dto;

import com.autooglasi.entity.AdStatus;
import com.autooglasi.entity.Advertisement;
import com.autooglasi.entity.FuelType;
import com.autooglasi.entity.TransmissionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Form-backing objekat za kreiranje/izmenu oglasa. */
public class AdvertisementForm {

    private Long id;

    @NotBlank(message = "Naslov je obavezan")
    @Size(max = 150)
    private String title;

    @Size(max = 4000)
    private String description;

    @NotNull(message = "Cena je obavezna")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cena mora biti veća od 0")
    private BigDecimal price;

    @NotNull(message = "Godina je obavezna")
    @Min(value = 1900, message = "Godina mora biti >= 1900")
    @Max(value = 2100, message = "Neispravna godina")
    private Integer year;

    @NotNull(message = "Kilometraža je obavezna")
    @Min(value = 0, message = "Kilometraža ne može biti negativna")
    private Integer mileage;

    @NotNull(message = "Vrsta goriva je obavezna")
    private FuelType fuelType;

    @NotNull(message = "Tip menjača je obavezan")
    private TransmissionType transmission;

    @Size(max = 40)
    private String color;

    @Min(value = 0, message = "Snaga ne može biti negativna")
    private Integer enginePower;

    @Size(max = 500)
    private String imageUrl;

    @NotNull(message = "Status je obavezan")
    private AdStatus status = AdStatus.AKTIVAN;

    @NotNull(message = "Model vozila je obavezan")
    private Long carModelId;

    @NotNull(message = "Grad je obavezan")
    private Long cityId;

    private Set<Long> featureIds = new HashSet<>();

    public AdvertisementForm() {
    }

    /** Mapiranje iz entiteta u formu (za edit). */
    public static AdvertisementForm fromEntity(Advertisement ad) {
        AdvertisementForm f = new AdvertisementForm();
        f.id = ad.getId();
        f.title = ad.getTitle();
        f.description = ad.getDescription();
        f.price = ad.getPrice();
        f.year = ad.getYear();
        f.mileage = ad.getMileage();
        f.fuelType = ad.getFuelType();
        f.transmission = ad.getTransmission();
        f.color = ad.getColor();
        f.enginePower = ad.getEnginePower();
        f.imageUrl = ad.getImageUrl();
        f.status = ad.getStatus();
        f.carModelId = ad.getCarModel() != null ? ad.getCarModel().getId() : null;
        f.cityId = ad.getCity() != null ? ad.getCity().getId() : null;
        f.featureIds = ad.getFeatures().stream().map(x -> x.getId()).collect(Collectors.toSet());
        return f;
    }

    // ===== Getteri / Setteri =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public TransmissionType getTransmission() {
        return transmission;
    }

    public void setTransmission(TransmissionType transmission) {
        this.transmission = transmission;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(Integer enginePower) {
        this.enginePower = enginePower;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AdStatus getStatus() {
        return status;
    }

    public void setStatus(AdStatus status) {
        this.status = status;
    }

    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Set<Long> getFeatureIds() {
        return featureIds;
    }

    public void setFeatureIds(Set<Long> featureIds) {
        this.featureIds = featureIds;
    }
}
