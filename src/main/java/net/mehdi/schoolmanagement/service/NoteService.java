package net.mehdi.schoolmanagement.service;

import net.mehdi.schoolmanagement.model.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {

    List<Note> getAllNotes();

    Optional<Note> getNoteById(Long id);

    void addNote(Note note);

    void updateNote(Note note);

    void deleteNote(Long id);


    List<Note> getNotesByStudent(Long studentId);

    List<Note> getNotesByMatiere(Long matiereId);

    Double getAverageByStudent(Long studentId);

    Double getWeightedAverageByStudent(Long studentId);

    List<Note> searchByStudentNom(String keyword);
}