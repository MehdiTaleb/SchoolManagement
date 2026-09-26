package net.mehdi.schoolmanagement.controller;

import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Note;
import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.service.ClasseService;
import net.mehdi.schoolmanagement.service.NoteService;
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

    @Autowired private StudentService studentService;
    @Autowired private ClasseService classeService;
    @Autowired private NoteService noteService;

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

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("classes", classeService.getAllClasses());   // ✅ AJOUTER
        return "students/students-list";
    }
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classeService.getAllClasses());
        model.addAttribute("formTitle", "Ajouter un étudiant");
        return "students/student-form";
    }

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

    @GetMapping("/search")
    public String searchStudents(@RequestParam String keyword, Model model) {
        model.addAttribute("students", studentService.searchByNom(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("classes", classeService.getAllClasses());   // ✅ AJOUTER
        return "students/students-list";
    }

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

    @GetMapping("/{id}/notes")
    public String studentNotes(@PathVariable Long id, Model model) {

        Optional<Student> student = studentService.getStudentById(id);

        if (student.isEmpty()) {
            return "redirect:/students";
        }

        Student s = student.get();
        List<Note> notes = noteService.getNotesByStudent(id);

        // Calcul de la moyenne pondérée
        Double average = noteService.getWeightedAverageByStudent(id);

        model.addAttribute("student", s);
        model.addAttribute("notes", notes);
        model.addAttribute("average", average);

        return "students/student-notes";
    }

    @GetMapping("/filter")
    public String filterByClasse(
            @RequestParam(required = false) Long classeId,
            Model model) {

        List<Student> students;

        if (classeId == null) {
            students = studentService.getAllStudents();
        } else {
            students = studentService.getStudentsByClasse(classeId);
        }

        model.addAttribute("students", students);
        model.addAttribute("classes", classeService.getAllClasses());
        model.addAttribute("selectedClasseId", classeId);   // ✅ pour garder la sélection

        return "students/students-list";
    }
}