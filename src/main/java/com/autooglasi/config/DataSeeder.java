package com.autooglasi.config;

import com.autooglasi.entity.*;
import com.autooglasi.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Inicijalno punjenje baze (role, nalozi, katalog, par oglasa).
 * Pokrece se jednom - ako vec postoje role, preskace.
 *
 * Demo nalozi:  admin / admin   (ROLE_ADMIN, ROLE_USER)
 *               pera  / pera    (ROLE_USER)
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;
    private final CityRepository cityRepository;
    private final FeatureRepository featureRepository;
    private final AdvertisementRepository advertisementRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository,
                      BrandRepository brandRepository, CarModelRepository carModelRepository,
                      CityRepository cityRepository, FeatureRepository featureRepository,
                      AdvertisementRepository advertisementRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.cityRepository = cityRepository;
        this.featureRepository = featureRepository;
        this.advertisementRepository = advertisementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            return; // vec inicijalizovano
        }

        // ===== Role =====
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
        Role userRole = roleRepository.save(new Role("ROLE_USER"));

        // ===== Korisnici =====
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@auto-oglasi.rs");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFirstName("Glavni");
        admin.setLastName("Administrator");
        admin.setPhone("+381601112233");
        admin.addRole(adminRole);
        admin.addRole(userRole);
        userRepository.save(admin);

        User pera = new User();
        pera.setUsername("pera");
        pera.setEmail("pera@example.com");
        pera.setPassword(passwordEncoder.encode("pera"));
        pera.setFirstName("Pera");
        pera.setLastName("Perić");
        pera.setPhone("+381641234567");
        pera.addRole(userRole);
        userRepository.save(pera);

        // ===== Marke i modeli (OneToMany) =====
        Brand bmw = brandRepository.save(new Brand("BMW", "Nemačka"));
        Brand audi = brandRepository.save(new Brand("Audi", "Nemačka"));
        Brand vw = brandRepository.save(new Brand("Volkswagen", "Nemačka"));
        Brand toyota = brandRepository.save(new Brand("Toyota", "Japan"));

        CarModel bmw3 = carModelRepository.save(new CarModel("Serija 3", bmw));
        CarModel bmw5 = carModelRepository.save(new CarModel("Serija 5", bmw));
        CarModel audiA4 = carModelRepository.save(new CarModel("A4", audi));
        carModelRepository.save(new CarModel("A6", audi));
        CarModel golf = carModelRepository.save(new CarModel("Golf 7", vw));
        carModelRepository.save(new CarModel("Passat", vw));
        CarModel corolla = carModelRepository.save(new CarModel("Corolla", toyota));

        // ===== Gradovi (OneToMany) =====
        City bg = cityRepository.save(new City("Beograd", "11000"));
        City ns = cityRepository.save(new City("Novi Sad", "21000"));
        City nis = cityRepository.save(new City("Niš", "18000"));
        cityRepository.save(new City("Kragujevac", "34000"));

        // ===== Oprema (ManyToMany) =====
        Feature abs = featureRepository.save(new Feature("ABS"));
        Feature klima = featureRepository.save(new Feature("Klima"));
        Feature navi = featureRepository.save(new Feature("Navigacija"));
        Feature koza = featureRepository.save(new Feature("Kožni enterijer"));
        featureRepository.save(new Feature("Parking senzori"));
        featureRepository.save(new Feature("Tempomat"));

        // ===== Oglasi =====
        advertisementRepository.save(buildAd(
                "BMW Serija 3 320d - odlično stanje", bmw3, bg, pera,
                new BigDecimal("15900"), 2018, 145000, FuelType.DIZEL, TransmissionType.AUTOMATSKI,
                "Crna", 140, Set.of(abs, klima, navi, koza),
                "https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800"));

        advertisementRepository.save(buildAd(
                "Audi A4 2.0 TDI S-line", audiA4, ns, pera,
                new BigDecimal("13500"), 2016, 198000, FuelType.DIZEL, TransmissionType.MANUELNI,
                "Siva", 110, Set.of(abs, klima, navi),
                "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800"));

        advertisementRepository.save(buildAd(
                "VW Golf 7 1.6 TDI - prvi vlasnik", golf, nis, admin,
                new BigDecimal("11200"), 2017, 167000, FuelType.DIZEL, TransmissionType.MANUELNI,
                "Bela", 85, Set.of(abs, klima),
                "https://images.unsplash.com/photo-1572811169493-cb91e8d7f1f7?w=800"));

        advertisementRepository.save(buildAd(
                "Toyota Corolla Hybrid - kao nova", corolla, bg, admin,
                new BigDecimal("18900"), 2021, 54000, FuelType.HIBRID, TransmissionType.AUTOMATSKI,
                "Plava", 90, Set.of(abs, klima, navi),
                "https://images.unsplash.com/photo-1623869675781-80aa31012a5a?w=800"));

        advertisementRepository.save(buildAd(
                "BMW Serija 5 530i M paket", bmw5, ns, pera,
                new BigDecimal("24500"), 2019, 98000, FuelType.BENZIN, TransmissionType.AUTOMATSKI,
                "Crna", 185, Set.of(abs, klima, navi, koza),
                "https://images.unsplash.com/photo-1556189250-72ba954cfc2b?w=800"));
    }

    private Advertisement buildAd(String title, CarModel model, City city, User seller,
                                  BigDecimal price, int year, int mileage, FuelType fuel,
                                  TransmissionType trans, String color, int power,
                                  Set<Feature> features, String imageUrl) {
        Advertisement ad = new Advertisement();
        ad.setTitle(title);
        ad.setDescription("Vozilo u odličnom stanju, redovno servisirano, bez ulaganja. " +
                "Moguća zamena uz doplatu. Kontakt telefon u oglasu.");
        ad.setCarModel(model);
        ad.setCity(city);
        ad.setSeller(seller);
        ad.setPrice(price);
        ad.setYear(year);
        ad.setMileage(mileage);
        ad.setFuelType(fuel);
        ad.setTransmission(trans);
        ad.setColor(color);
        ad.setEnginePower(power);
        ad.setImageUrl(imageUrl);
        ad.setStatus(AdStatus.AKTIVAN);
        ad.setFeatures(new java.util.HashSet<>(features));
        return ad;
    }
}
