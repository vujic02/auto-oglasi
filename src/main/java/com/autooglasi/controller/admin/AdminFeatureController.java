package com.autooglasi.controller.admin;

import com.autooglasi.entity.Feature;
import com.autooglasi.service.FeatureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD nad opremom vozila (admin). */
@Controller
@RequestMapping("/admin/features")
public class AdminFeatureController {

    private final FeatureService featureService;

    public AdminFeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("features", featureService.findAll());
        return "admin/features/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("feature", new Feature());
        model.addAttribute("editMode", false);
        return "admin/features/form";
    }

    @PostMapping
    public String create(@RequestParam String name, RedirectAttributes ra) {
        featureService.save(new Feature(name.trim()));
        ra.addFlashAttribute("successMessage", "Oprema je dodata.");
        return "redirect:/admin/features";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("feature", featureService.findById(id));
        model.addAttribute("editMode", true);
        return "admin/features/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @RequestParam String name, RedirectAttributes ra) {
        Feature feature = featureService.findById(id);
        feature.setName(name.trim());
        featureService.save(feature);
        ra.addFlashAttribute("successMessage", "Oprema je izmenjena.");
        return "redirect:/admin/features";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        featureService.delete(id);
        ra.addFlashAttribute("successMessage", "Oprema je obrisana.");
        return "redirect:/admin/features";
    }
}
