package net.mehdi.schoolmanagement.repository;

import net.mehdi.schoolmanagement.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // =========================
    // Requêtes avec JOIN FETCH (évite LazyInitializationException)
    // =========================

    @Query("""
    SELECT n FROM Note n
    JOIN FETCH n.student
    JOIN FETCH n.matiere
    WHERE n.student.id = :studentId
    ORDER BY n.matiere.nom ASC, n.dateEvaluation DESC
""")
    List<Note> findByStudentIdWithDetails(@Param("studentId") Long studentId);

    @Query("""
        SELECT n FROM Note n
        JOIN FETCH n.student
        JOIN FETCH n.matiere
        ORDER BY n.dateEvaluation DESC
    """)
    List<Note> findAllWithStudentAndMatiere();

    @Query("""
        SELECT n FROM Note n
        JOIN FETCH n.student
        JOIN FETCH n.matiere
        WHERE n.id = :id
    """)
    Optional<Note> findByIdWithRelations(@Param("id") Long id);

    // =========================
    // Recherche par étudiant / matière
    // =========================

    @Query("""
        SELECT n FROM Note n
        JOIN FETCH n.student s
        JOIN FETCH n.matiere
        WHERE s.id = :studentId
        ORDER BY n.dateEvaluation DESC
    """)
    List<Note> findByStudentId(@Param("studentId") Long studentId);

    @Query("""
        SELECT n FROM Note n
        JOIN FETCH n.student
        JOIN FETCH n.matiere m
        WHERE m.id = :matiereId
        ORDER BY n.dateEvaluation DESC
    """)
    List<Note> findByMatiereId(@Param("matiereId") Long matiereId);

    // =========================
    // Recherche par nom d'étudiant
    // =========================

    @Query("""
        SELECT n FROM Note n
        JOIN FETCH n.student s
        JOIN FETCH n.matiere
        WHERE LOWER(s.nom) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY n.dateEvaluation DESC
    """)
    List<Note> searchByStudentNom(@Param("keyword") String keyword);

    // =========================
    // Calcul de moyennes
    // =========================

    @Query("""
        SELECT AVG(n.valeur)
        FROM Note n
        WHERE n.student.id = :studentId
    """)
    Double averageByStudent(@Param("studentId") Long studentId);

    @Query("""
        SELECT SUM(n.valeur * n.matiere.coefficient) / SUM(n.matiere.coefficient)
        FROM Note n
        WHERE n.student.id = :studentId
    """)
    Double weightedAverageByStudent(@Param("studentId") Long studentId);
}