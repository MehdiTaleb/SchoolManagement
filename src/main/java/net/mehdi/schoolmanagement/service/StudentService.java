package net.mehdi.schoolmanagement.service;

import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // =========================
    // CRUD
    // =========================

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student addStudent(Student student) {

        if (student.getEmail() != null &&
                studentRepository.findByEmail(student.getEmail()).isPresent()) {

            throw new IllegalArgumentException(
                    "Un étudiant avec cet email existe déjà"
            );
        }

        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {

        if (!studentRepository.existsById(student.getId())) {
            throw new IllegalArgumentException("Étudiant non trouvé");
        }

        student.setUpdatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Étudiant non trouvé");
        }

        studentRepository.deleteById(id);
    }

    // =========================
    // Recherche
    // =========================

    public List<Student> searchByNom(String nom) {
        return studentRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Student> searchByPrenom(String prenom) {
        return studentRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // =========================
    // Gestion Classe
    // =========================

    public List<Student> getStudentsByClasse(Long classeId) {
        return studentRepository.findByClasseId(classeId);
    }

    public void assignStudentToClasse(Student student, Classe classe) {

        student.setClasse(classe);
        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(student);
    }

    public void removeStudentFromClasse(Student student) {

        student.setClasse(null);
        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(student);
    }
}