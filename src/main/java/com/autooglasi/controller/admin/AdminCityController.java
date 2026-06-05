package com.autooglasi.controller.admin;

import com.autooglasi.entity.City;
import com.autooglasi.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD nad gradovima (admin). */
@Controller
@RequestMapping("/admin/cities")
public class AdminCityController {

    private final CityService cityService;

    public AdminCityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "admin/cities/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("city", new City());
        model.addAttribute("editMode", false);
        return "admin/cities/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String postalCode,
                         RedirectAttributes ra) {
        cityService.save(new City(name.trim(), postalCode));
        ra.addFlashAttribute("successMessage", "Grad je dodat.");
        return "redirect:/admin/cities";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("city", cityService.findById(id));
        model.addAttribute("editMode", true);
        return "admin/cities/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @RequestParam String name,
                         @RequestParam(required = false) String postalCode, RedirectAttributes ra) {
        City city = cityService.findById(id);
        city.setName(name.trim());
        city.setPostalCode(postalCode);
        cityService.save(city);
        ra.addFlashAttribute("successMessage", "Grad je izmenjen.");
        return "redirect:/admin/cities";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        cityService.delete(id);
        ra.addFlashAttribute("successMessage", "Grad je obrisan.");
        return "redirect:/admin/cities";
    }
}
