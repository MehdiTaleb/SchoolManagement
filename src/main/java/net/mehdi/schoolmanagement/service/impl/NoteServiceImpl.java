package net.mehdi.schoolmanagement.service.impl;

import net.mehdi.schoolmanagement.model.Note;
import net.mehdi.schoolmanagement.repository.NoteRepository;
import net.mehdi.schoolmanagement.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // =========================
    // Lecture
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<Note> getAllNotes() {
        return noteRepository.findAllWithStudentAndMatiere();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findByIdWithRelations(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> getNotesByStudent(Long studentId) {
        return noteRepository.findByStudentIdWithDetails(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> getNotesByMatiere(Long matiereId) {
        return noteRepository.findByMatiereId(matiereId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> searchByStudentNom(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllNotes();
        }
        return noteRepository.searchByStudentNom(keyword.trim());
    }

    // =========================
    // Moyennes
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Double getAverageByStudent(Long studentId) {
        Double avg = noteRepository.averageByStudent(studentId);
        return avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getWeightedAverageByStudent(Long studentId) {
        Double avg = noteRepository.weightedAverageByStudent(studentId);
        return avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;
    }

    // =========================
    // Écriture
    // =========================

    @Override
    public void addNote(Note note) {
        validateNote(note);
        noteRepository.save(note);
    }

    @Override
    public void updateNote(Note note) {
        if (note.getId() == null) {
            throw new IllegalArgumentException("ID de la note manquant.");
        }

        noteRepository.findById(note.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Note introuvable avec l'ID : " + note.getId()));

        validateNote(note);
        noteRepository.save(note);
    }


    @Override
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Note introuvable avec l'ID : " + id));
        noteRepository.delete(note);
    }

    // =========================
    // Validation
    // =========================

    private void validateNote(Note note) {

        if (note.getValeur() == null) {
            throw new IllegalArgumentException("La valeur de la note est obligatoire.");
        }

        if (note.getValeur() < 0 || note.getValeur() > 20) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
        }

        if (note.getStudent() == null) {
            throw new IllegalArgumentException("L'étudiant est obligatoire.");
        }

        if (note.getMatiere() == null) {
            throw new IllegalArgumentException("La matière est obligatoire.");
        }
    }
}