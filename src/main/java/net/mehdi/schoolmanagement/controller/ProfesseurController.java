package net.mehdi.schoolmanagement.controller;

import jakarta.validation.Valid;
import net.mehdi.schoolmanagement.model.Professeur;
import net.mehdi.schoolmanagement.service.ClasseService;
import net.mehdi.schoolmanagement.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Controller
@RequestMapping("/professeurs")
public class ProfesseurController {

    @Autowired
    private ProfesseurService professeurService;

    @Autowired
    private ClasseService classeService;

    // =========================
    // Conversion String -> Classe pour le <select>
    // =========================

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                net.mehdi.schoolmanagement.model.Classe.class,
                new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) {
                        if (text == null || text.isEmpty()) {
                            setValue(null);
                        } else {
                            Long id = Long.valueOf(text);
                            setValue(classeService.getClasseById(id).orElse(null));
                        }
                    }
                }
        );
    }

    // =========================
    // Liste
    // =========================

    @GetMapping
    public String listProfesseurs(Model model) {
        model.addAttribute(
                "professeurs",
                professeurService.getAllProfesseurs()
        );
        return "professeurs/professeurs-list";
    }

    // =========================
    // Formulaire ajout
    // =========================

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("professeur", new Professeur());
        model.addAttribute("classes", classeService.getAllClasses());
        model.addAttribute("formTitle", "Ajouter un professeur");
        return "professeurs/professeur-form";
    }

    // =========================
    // Ajouter
    // =========================

    @PostMapping("/add")
    public String addProfesseur(
            @Valid @ModelAttribute Professeur professeur,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            professeurService.addProfesseur(professeur);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Professeur ajouté avec succès !"
            );
            return "redirect:/professeurs";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("professeur", professeur);
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formTitle", "Ajouter un professeur");
            return "professeurs/professeur-form";
        }
    }

    // =========================
    // Formulaire modification
    // =========================

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Optional<Professeur> professeur = professeurService.getProfesseurById(id);

        if (professeur.isEmpty()) {
            return "redirect:/professeurs";
        }

        model.addAttribute("professeur", professeur.get());
        model.addAttribute("classes", classeService.getAllClasses());
        model.addAttribute("formTitle", "Modifier le professeur");
        return "professeurs/professeur-form";
    }

    // =========================
    // Modifier
    // =========================

    @PostMapping("/edit")
    public String updateProfesseur(
            @Valid @ModelAttribute Professeur professeur,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            professeurService.updateProfesseur(professeur);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Professeur modifié avec succès !"
            );
            return "redirect:/professeurs";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("professeur", professeur);
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formTitle", "Modifier le professeur");
            return "professeurs/professeur-form";
        }
    }

    // =========================
    // Supprimer
    // =========================

    @GetMapping("/delete/{id}")
    public String deleteProfesseur(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            professeurService.deleteProfesseur(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Professeur supprimé avec succès !"
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/professeurs";
    }

    // =========================
    // Recherche
    // =========================

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

    // =========================
    // Détails
    // =========================

    @GetMapping("/{id}/details")
    public String details(
            @PathVariable Long id,
            Model model) {

        Optional<Professeur> professeur = professeurService.getProfesseurById(id);

        if (professeur.isEmpty()) {
            return "redirect:/professeurs";
        }

        model.addAttribute("professeur", professeur.get());
        return "professeurs/professeur-details";
    }
}