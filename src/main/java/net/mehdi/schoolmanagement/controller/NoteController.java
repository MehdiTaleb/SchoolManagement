package net.mehdi.schoolmanagement.controller;

import jakarta.validation.Valid;
import net.mehdi.schoolmanagement.model.Matiere;
import net.mehdi.schoolmanagement.model.Note;
import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.service.MatiereService;
import net.mehdi.schoolmanagement.service.NoteService;
import net.mehdi.schoolmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.util.Optional;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired private NoteService noteService;
    @Autowired private StudentService studentService;
    @Autowired private MatiereService matiereService;

    // =========================
    // Conversion String -> Student et String -> Matiere
    // =========================

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Student.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    Long id = Long.valueOf(text);
                    setValue(studentService.getStudentById(id).orElse(null));
                }
            }
        });

        binder.registerCustomEditor(Matiere.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    Long id = Long.valueOf(text);
                    setValue(matiereService.getMatiereById(id).orElse(null));
                }
            }
        });
    }

    // =========================
    // Liste
    // =========================

    @GetMapping
    public String listNotes(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "notes/notes-list";
    }

    // =========================
    // Formulaire ajout
    // =========================

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("matieres", matiereService.getAllMatieres());
        model.addAttribute("formTitle", "Ajouter une note");
        return "notes/note-form";
    }

    // =========================
    // Ajouter
    // =========================

    @PostMapping("/add")
    public String addNote(
            @Valid @ModelAttribute Note note,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            noteService.addNote(note);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Note ajoutée avec succès !"
            );
            return "redirect:/notes";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("note", note);
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("matieres", matiereService.getAllMatieres());
            model.addAttribute("formTitle", "Ajouter une note");
            return "notes/note-form";
        }
    }

    // =========================
    // Formulaire modification
    // =========================

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Note> note = noteService.getNoteById(id);

        if (note.isEmpty()) {
            return "redirect:/notes";
        }

        model.addAttribute("note", note.get());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("matieres", matiereService.getAllMatieres());
        model.addAttribute("formTitle", "Modifier la note");
        return "notes/note-form";
    }

    // =========================
    // Modifier
    // =========================

    @PostMapping("/edit")
    public String updateNote(
            @Valid @ModelAttribute Note note,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            noteService.updateNote(note);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Note modifiée avec succès !"
            );
            return "redirect:/notes";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("note", note);
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("matieres", matiereService.getAllMatieres());
            model.addAttribute("formTitle", "Modifier la note");
            return "notes/note-form";
        }
    }

    // =========================
    // Supprimer
    // =========================

    @GetMapping("/delete/{id}")
    public String deleteNote(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            noteService.deleteNote(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Note supprimée avec succès !"
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }

        return "redirect:/notes";
    }

    // =========================
    // Détails
    // =========================

    @GetMapping("/{id}/details")
    public String details(@PathVariable Long id, Model model) {
        Optional<Note> note = noteService.getNoteById(id);

        if (note.isEmpty()) {
            return "redirect:/notes";
        }

        model.addAttribute("note", note.get());
        return "notes/note-details";
    }
}