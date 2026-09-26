package net.mehdi.schoolmanagement.service.impl;

import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.repository.StudentRepository;
import net.mehdi.schoolmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service                                        // ✅ Annotation ICI
@Transactional
public class StudentServiceImpl implements StudentService {   // ✅ implements

    @Autowired
    private StudentRepository studentRepository;

    // =========================
    // CRUD
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAllWithClasse();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
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

    @Override
    public Student updateStudent(Student student) {

        if (!studentRepository.existsById(student.getId())) {
            throw new IllegalArgumentException("Étudiant non trouvé");
        }

        student.setUpdatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Étudiant non trouvé");
        }

        studentRepository.deleteById(id);
    }

    // =========================
    // Recherche
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<Student> searchByNom(String nom) {
        return studentRepository.findByNomContainingIgnoreCase(nom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> searchByPrenom(String prenom) {
        return studentRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // =========================
    // Gestion Classe
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<Student> getStudentsByClasse(Long classeId) {
        return studentRepository.findByClasseIdWithClasse(classeId);
    }

    @Override
    public void assignStudentToClasse(Student student, Classe classe) {
        student.setClasse(classe);
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.save(student);
    }

    @Override
    public void removeStudentFromClasse(Student student) {
        student.setClasse(null);
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.save(student);
    }
}