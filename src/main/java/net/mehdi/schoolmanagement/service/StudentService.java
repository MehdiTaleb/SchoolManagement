package net.mehdi.schoolmanagement.service;

import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    // CRUD
    List<Student> getAllStudents();
    Optional<Student> getStudentById(Long id);
    Student addStudent(Student student);
    Student updateStudent(Student student);
    void deleteStudent(Long id);

    // Recherche
    List<Student> searchByNom(String nom);
    List<Student> searchByPrenom(String prenom);
    Optional<Student> getStudentByEmail(String email);

    // Classe
    List<Student> getStudentsByClasse(Long classeId);
    void assignStudentToClasse(Student student, Classe classe);
    void removeStudentFromClasse(Student student);
}