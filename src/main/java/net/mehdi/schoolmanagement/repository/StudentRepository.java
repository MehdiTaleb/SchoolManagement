package net.mehdi.schoolmanagement.repository;

import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.model.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
    SELECT s FROM Student s
    LEFT JOIN FETCH s.classe
    WHERE s.classe.id = :classeId
    ORDER BY s.nom ASC, s.prenom ASC
""")
    List<Student> findByClasseIdWithClasse(@Param("classeId") Long classeId);

    @Query("""
    SELECT s FROM Student s
    LEFT JOIN FETCH s.classe
    ORDER BY s.nom ASC, s.prenom ASC
""")
    List<Student> findAllWithClasse();
}