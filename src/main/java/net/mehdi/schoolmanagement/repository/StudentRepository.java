package net.mehdi.schoolmanagement.repository;

import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.model.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    List<Student> findByNomContainingIgnoreCase(String nom);

    List<Student> findByPrenomContainingIgnoreCase(String prenom);

    List<Student> findByClasse(Classe classe);

    List<Student> findByClasseId(Long classeId);
}