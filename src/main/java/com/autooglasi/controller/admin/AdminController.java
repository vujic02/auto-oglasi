package com.autooglasi.controller.admin;

import com.autooglasi.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Admin kontrolna tabla (dostupna samo ROLE_ADMIN - vidi SecurityConfig). */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BrandService brandService;
    private final CarModelService carModelService;
    private final CityService cityService;
    private final FeatureService featureService;
    private final UserService userService;

    public AdminController(BrandService brandService, CarModelService carModelService,
                          CityService cityService, FeatureService featureService,
                          UserService userService) {
        this.brandService = brandService;
        this.carModelService = carModelService;
        this.cityService = cityService;
        this.featureService = featureService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("brandCount", brandService.findAll().size());
        model.addAttribute("modelCount", carModelService.findAll().size());
        model.addAttribute("cityCount", cityService.findAll().size());
        model.addAttribute("featureCount", featureService.findAll().size());
        model.addAttribute("userCount", userService.findAll().size());
        return "admin/dashboard";
    }
}
