package com.autooglasi.controller.admin;

import com.autooglasi.entity.Brand;
import com.autooglasi.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD nad markama automobila (admin). */
@Controller
@RequestMapping("/admin/brands")
public class AdminBrandController {

    private final BrandService brandService;

    public AdminBrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "admin/brands/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("brand", new Brand());
        model.addAttribute("editMode", false);
        return "admin/brands/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String countryOfOrigin,
                         RedirectAttributes ra) {
        brandService.save(new Brand(name.trim(), countryOfOrigin));
        ra.addFlashAttribute("successMessage", "Marka je dodata.");
        return "redirect:/admin/brands";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("brand", brandService.findById(id));
        model.addAttribute("editMode", true);
        return "admin/brands/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam(required = false) String countryOfOrigin,
                         RedirectAttributes ra) {
        Brand brand = brandService.findById(id);
        brand.setName(name.trim());
        brand.setCountryOfOrigin(countryOfOrigin);
        brandService.save(brand);
        ra.addFlashAttribute("successMessage", "Marka je izmenjena.");
        return "redirect:/admin/brands";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        brandService.delete(id);
        ra.addFlashAttribute("successMessage", "Marka je obrisana.");
        return "redirect:/admin/brands";
    }
}
