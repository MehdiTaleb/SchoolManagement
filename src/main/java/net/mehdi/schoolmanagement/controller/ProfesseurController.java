package net.mehdi.schoolmanagement.controller;

import jakarta.validation.Valid;
import net.mehdi.schoolmanagement.model.Professeur;
import net.mehdi.schoolmanagement.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/professeurs")
public class ProfesseurController {

    @Autowired
    private ProfesseurService professeurService;

    @GetMapping
    public String listProfesseurs(Model model) {

        model.addAttribute(
                "professeurs",
                professeurService.getAllProfesseurs()
        );

        return "professeurs/professeurs-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {

        model.addAttribute("professeur", new Professeur());
        model.addAttribute("formTitle", "Ajouter un professeur");

        return "professeurs/professeur-form";
    }

    @PostMapping("/add")
    public String addProfesseur(
            @Valid @ModelAttribute Professeur professeur,
            Model model) {

        try {

            professeurService.addProfesseur(professeur);

            return "redirect:/professeurs";

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formTitle", "Ajouter un professeur");

            return "professeurs/professeur-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Professeur professeur = professeurService
                .getProfesseurById(id)
                .orElse(null);

        if (professeur == null) {
            return "redirect:/professeurs";
        }

        model.addAttribute("professeur", professeur);
        model.addAttribute("formTitle", "Modifier le professeur");

        return "professeurs/professeur-form";
    }

    @PostMapping("/edit")
    public String updateProfesseur(
            @Valid @ModelAttribute Professeur professeur,
            Model model) {

        try {

            professeurService.updateProfesseur(professeur);

            return "redirect:/professeurs";

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formTitle", "Modifier le professeur");

            return "professeurs/professeur-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProfesseur(
            @PathVariable Long id) {

        try {
            professeurService.deleteProfesseur(id);
        } catch (IllegalArgumentException ignored) {
        }

        return "redirect:/professeurs";
    }

    @GetMapping("/search")
    public String searchProfesseurs(
            @RequestParam String keyword,
            Model model) {

        model.addAttribute(
                "professeurs",
                professeurService.searchByNom(keyword)
        );

        model.addAttribute("keyword", keyword);

        return "professeurs/professeurs-list";
    }

    @GetMapping("/{id}/details")
    public String details(
            @PathVariable Long id,
            Model model) {

        Professeur professeur = professeurService
                .getProfesseurById(id)
                .orElse(null);

        if (professeur == null) {
            return "redirect:/professeurs";
        }

        model.addAttribute("professeur", professeur);

        return "professeurs/professeur-details";
    }
}