package net.mehdi.schoolmanagement.controller;

import jakarta.validation.Valid;
import net.mehdi.schoolmanagement.model.Matiere;
import net.mehdi.schoolmanagement.model.Professeur;
import net.mehdi.schoolmanagement.service.MatiereService;
import net.mehdi.schoolmanagement.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/matieres")
public class MatiereController {

    @Autowired
    private MatiereService matiereService;

    @Autowired
    private ProfesseurService professeurService;

    @GetMapping
    public String listMatieres(Model model) {

        model.addAttribute(
                "matieres",
                matiereService.getAllMatieres()
        );

        return "matieres/matieres-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {

        model.addAttribute("matiere", new Matiere());

        model.addAttribute(
                "professeurs",
                professeurService.getAllProfesseurs()
        );

        model.addAttribute(
                "formTitle",
                "Ajouter une matière"
        );

        return "matieres/matiere-form";
    }

    @PostMapping("/add")
    public String addMatiere(
            @Valid @ModelAttribute Matiere matiere,
            Model model) {

        try {

            matiereService.addMatiere(matiere);

            return "redirect:/matieres";

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());

            model.addAttribute(
                    "professeurs",
                    professeurService.getAllProfesseurs()
            );

            model.addAttribute(
                    "formTitle",
                    "Ajouter une matière"
            );

            return "matieres/matiere-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Matiere matiere = matiereService
                .getMatiereById(id)
                .orElse(null);

        if (matiere == null) {
            return "redirect:/matieres";
        }

        model.addAttribute("matiere", matiere);

        model.addAttribute(
                "professeurs",
                professeurService.getAllProfesseurs()
        );

        model.addAttribute(
                "formTitle",
                "Modifier la matière"
        );

        return "matieres/matiere-form";
    }

    @PostMapping("/edit")
    public String updateMatiere(
            @Valid @ModelAttribute Matiere matiere,
            Model model) {

        try {

            matiereService.updateMatiere(matiere);

            return "redirect:/matieres";

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());

            model.addAttribute(
                    "professeurs",
                    professeurService.getAllProfesseurs()
            );

            model.addAttribute(
                    "formTitle",
                    "Modifier la matière"
            );

            return "matieres/matiere-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMatiere(
            @PathVariable Long id) {

        try {
            matiereService.deleteMatiere(id);
        } catch (IllegalArgumentException ignored) {
        }

        return "redirect:/matieres";
    }

    @GetMapping("/search")
    public String searchMatieres(
            @RequestParam String keyword,
            Model model) {

        model.addAttribute(
                "matieres",
                matiereService.searchByNom(keyword)
        );

        model.addAttribute("keyword", keyword);

        return "matieres/matieres-list";
    }

    @GetMapping("/{id}/details")
    public String details(
            @PathVariable Long id,
            Model model) {

        Matiere matiere = matiereService
                .getMatiereById(id)
                .orElse(null);

        if (matiere == null) {
            return "redirect:/matieres";
        }

        model.addAttribute("matiere", matiere);

        return "matieres/matiere-details";
    }
}