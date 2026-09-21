package net.mehdi.schoolmanagement.controller;


import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Niveau;
import net.mehdi.schoolmanagement.service.ClasseService;
import jakarta.validation.Valid;
import net.mehdi.schoolmanagement.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/classes")
public class ClasseController {

    @Autowired
    private ClasseService classeService;
    @Autowired
    private ProfesseurService professeurService;

    // Lister toutes les classes
    @GetMapping
    public String listClasses(Model model) {
        List<Classe> classes = classeService.getAllClasses();
        model.addAttribute("classes", classes);
        model.addAttribute("professeurs", professeurService.getAllProfesseurs());
        model.addAttribute("niveaux", Niveau.values());
        return "classes/classes-list";
    }

    // Afficher le formulaire d'ajout
    @GetMapping("add")
    public String showAddForm(Model model) {
        model.addAttribute("classe", new Classe());
        model.addAttribute("niveaux", Niveau.values());
        model.addAttribute("professeurs", professeurService.getAllProfesseurs());
        model.addAttribute("formTitle", "Ajouter une Classe");
        return "classes/classe-form";
    }

    // Ajouter une classe
    @PostMapping("add")
    public String addClass(
            //@Valid
            @ModelAttribute Classe classe,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("niveaux", Niveau.values());
            model.addAttribute("formTitle", "Ajouter une Classe");
            return "classes/classe-form";
        }

        try {
            classeService.addClasse(classe);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Classe ajoutée avec succès!");
            return "redirect:/classes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("niveaux", Niveau.values());
            return "classes/classe-form";
        }
    }

    // Éditer une classe
    @GetMapping("edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Classe> classe = classeService.getClasseById(id);
        if (classe.isPresent()) {
            model.addAttribute("classe", classe.get());
            model.addAttribute("niveaux", Niveau.values());
            model.addAttribute("professeurs", professeurService.getAllProfesseurs());
            model.addAttribute("formTitle", "Éditer Classe");
            return "classes/classe-form";
        }
        return "redirect:/classes";
    }

    @PostMapping("edit")
    public String editClasse(
            @Valid @ModelAttribute Classe classe,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("niveaux", Niveau.values());
            return "classes/classe-form";
        }

        try {
            classeService.updateClasse(classe);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Classe modifiée avec succès!");
            return "redirect:/classes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "classes/classe-form";
        }
    }

    // Supprimer une classe
    @GetMapping("delete/{id}")
    public String deleteClasse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            classeService.deleteClasse(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Classe supprimée avec succès!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/classes";
    }

    // Voir les détails d'une classe
    @GetMapping("{id}/details")
    public String getClasseDetails(@PathVariable Long id, Model model) {
        Optional<Classe> classe = classeService.getClasseById(id);
        if (classe.isPresent()) {
            model.addAttribute("classe", classe.get());
            model.addAttribute("professeurs", professeurService.getAllProfesseurs());
            model.addAttribute("students", classeService.getStudentsByClasse(id));
            model.addAttribute("moyenne", classeService.getMoyenneClasse(id));
            model.addAttribute("effectif", classeService.countStudentsByClasse(id));
            return "classes/classe-details";
        }
        return "redirect:/classes";
    }
}