package com.autooglasi.controller.admin;

import com.autooglasi.entity.CarModel;
import com.autooglasi.service.BrandService;
import com.autooglasi.service.CarModelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD nad modelima vozila (admin). Model pripada marki (ManyToOne). */
@Controller
@RequestMapping("/admin/models")
public class AdminCarModelController {

    private final CarModelService carModelService;
    private final BrandService brandService;

    public AdminCarModelController(CarModelService carModelService, BrandService brandService) {
        this.carModelService = carModelService;
        this.brandService = brandService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("models", carModelService.findAll());
        return "admin/models/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("carModel", new CarModel());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("editMode", false);
        return "admin/models/form";
    }

    @PostMapping
    public String create(@RequestParam String name, @RequestParam Long brandId, RedirectAttributes ra) {
        carModelService.save(new CarModel(name.trim(), null), brandId);
        ra.addFlashAttribute("successMessage", "Model je dodat.");
        return "redirect:/admin/models";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("carModel", carModelService.findById(id));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("editMode", true);
        return "admin/models/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @RequestParam String name,
                         @RequestParam Long brandId, RedirectAttributes ra) {
        CarModel cm = carModelService.findById(id);
        cm.setName(name.trim());
        carModelService.save(cm, brandId);
        ra.addFlashAttribute("successMessage", "Model je izmenjen.");
        return "redirect:/admin/models";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        carModelService.delete(id);
        ra.addFlashAttribute("successMessage", "Model je obrisan.");
        return "redirect:/admin/models";
    }
}
