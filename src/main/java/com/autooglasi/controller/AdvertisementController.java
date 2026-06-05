package com.autooglasi.controller;

import com.autooglasi.dto.AdvertisementForm;
import com.autooglasi.entity.*;
import com.autooglasi.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;

/** Pregled, pretraga i CRUD oglasa (jezgro aplikacije). */
@Controller
public class AdvertisementController {

    private static final int PAGE_SIZE = 6;

    private final AdvertisementService advertisementService;
    private final BrandService brandService;
    private final CarModelService carModelService;
    private final CityService cityService;
    private final FeatureService featureService;
    private final UserService userService;

    public AdvertisementController(AdvertisementService advertisementService, BrandService brandService,
                                   CarModelService carModelService, CityService cityService,
                                   FeatureService featureService, UserService userService) {
        this.advertisementService = advertisementService;
        this.brandService = brandService;
        this.carModelService = carModelService;
        this.cityService = cityService;
        this.featureService = featureService;
        this.userService = userService;
    }

    // ===== Lista + pretraga (pocetna i /ads) =====
    @GetMapping({"/", "/ads"})
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) Long brandId,
                       @RequestParam(required = false) String fuelType,
                       @RequestParam(required = false) BigDecimal minPrice,
                       @RequestParam(required = false) BigDecimal maxPrice,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {

        FuelType fuel = parseFuel(fuelType);
        Pageable pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE);
        Page<Advertisement> ads = advertisementService.search(q, brandId, fuel, minPrice, maxPrice, pageable);

        model.addAttribute("ads", ads);
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("q", q);
        model.addAttribute("brandId", brandId);
        model.addAttribute("fuelType", fuelType);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "ad/list";
    }

    // ===== Detalj oglasa =====
    @GetMapping("/ads/{id}")
    public String detail(@PathVariable Long id, Model model, Authentication authentication) {
        Advertisement ad = advertisementService.findById(id);
        model.addAttribute("ad", ad);

        boolean canModify = authentication != null && authentication.isAuthenticated()
                && advertisementService.canModify(ad, authentication.getName(), isAdmin(authentication));
        model.addAttribute("canModify", canModify);
        return "ad/detail";
    }

    // ===== Moji oglasi =====
    @GetMapping("/my-ads")
    public String myAds(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("ads", advertisementService.findBySeller(user.getId()));
        return "ad/my-ads";
    }

    // ===== Nova forma =====
    @GetMapping("/ads/new")
    public String createForm(Model model) {
        if (!model.containsAttribute("advertisementForm")) {
            model.addAttribute("advertisementForm", new AdvertisementForm());
        }
        populateReferenceData(model);
        model.addAttribute("editMode", false);
        return "ad/form";
    }

    @PostMapping("/ads")
    public String create(@Valid @ModelAttribute("advertisementForm") AdvertisementForm form,
                         BindingResult bindingResult, Model model, Principal principal,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateReferenceData(model);
            model.addAttribute("editMode", false);
            return "ad/form";
        }
        Advertisement saved = advertisementService.create(form, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Oglas je uspešno kreiran.");
        return "redirect:/ads/" + saved.getId();
    }

    // ===== Izmena forma =====
    @GetMapping("/ads/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
        Advertisement ad = advertisementService.findById(id);
        if (!advertisementService.canModify(ad, authentication.getName(), isAdmin(authentication))) {
            throw new AccessDeniedException("Nemate pravo da menjate ovaj oglas");
        }
        if (!model.containsAttribute("advertisementForm")) {
            model.addAttribute("advertisementForm", AdvertisementForm.fromEntity(ad));
        }
        populateReferenceData(model);
        model.addAttribute("editMode", true);
        model.addAttribute("adId", id);
        return "ad/form";
    }

    @PostMapping("/ads/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("advertisementForm") AdvertisementForm form,
                         BindingResult bindingResult, Model model, Principal principal,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateReferenceData(model);
            model.addAttribute("editMode", true);
            model.addAttribute("adId", id);
            return "ad/form";
        }
        advertisementService.update(id, form, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Oglas je uspešno izmenjen.");
        return "redirect:/ads/" + id;
    }

    // ===== Brisanje =====
    @PostMapping("/ads/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        advertisementService.delete(id, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Oglas je obrisan.");
        return "redirect:/my-ads";
    }

    // ===== Pomocne metode =====

    private void populateReferenceData(Model model) {
        model.addAttribute("models", carModelService.findAll());
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("features", featureService.findAll());
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("transmissions", TransmissionType.values());
        model.addAttribute("statuses", AdStatus.values());
    }

    private FuelType parseFuel(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return FuelType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
