package net.mehdi.schoolmanagement.controller;

import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.service.ClasseService;
import net.mehdi.schoolmanagement.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClasseService classeService;

    // =========================
    // Conversion String -> Classe pour le <select>
    // =========================

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Classe.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    Long id = Long.valueOf(text);
                    setValue(classeService.getClasseById(id).orElse(null));
                }
            }
        });
    }

    // =========================
    // Liste des étudiants
    // =========================

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students/students-list";
    }

    // =========================
    // Formulaire ajout
    // =========================

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classeService.getAllClasses());
        model.addAttribute("formTitle", "Ajouter un étudiant");
        return "students/student-form";
    }

    // =========================
    // Ajouter étudiant
    // =========================

    @PostMapping("/add")
    public String addStudent(
            @ModelAttribute Student student,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            studentService.addStudent(student);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Étudiant ajouté avec succès !"
            );
            return "redirect:/students";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formTitle", "Ajouter un étudiant");
            return "students/student-form";
        }
    }

    // =========================
    // Formulaire modification
    // =========================

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Optional<Student> student = studentService.getStudentById(id);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formTitle", "Modifier l'étudiant");
            return "students/student-form";
        }

        return "redirect:/students";
    }

    // =========================
    // Modifier étudiant
    // =========================

    @PostMapping("/edit")
    public String editStudent(
            @ModelAttribute Student student,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            studentService.updateStudent(student);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Étudiant modifié avec succès !"
            );
            return "redirect:/students";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formTitle", "Modifier l'étudiant");
            return "students/student-form";
        }
    }

    // =========================
    // Supprimer étudiant
    // =========================

    @GetMapping("/delete/{id}")
    public String deleteStudent(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Étudiant supprimé avec succès !"
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/students";
    }

    // =========================
    // Recherche
    // =========================

    @GetMapping("/search")
    public String searchStudents(
            @RequestParam String keyword,
            Model model) {

        List<Student> students = studentService.searchByNom(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "students/students-list";
    }

    // =========================
    // Détails étudiant
    // =========================

    @GetMapping("/{id}/details")
    public String studentDetails(
            @PathVariable Long id,
            Model model) {

        Optional<Student> student = studentService.getStudentById(id);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "students/student-details";
        }

        return "redirect:/students";
    }
}