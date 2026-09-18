package net.mehdi.schoolmanagement.repository;

import net.mehdi.schoolmanagement.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {

    Optional<Professeur> findByEmail(String email);

    List<Professeur> findByNomContainingIgnoreCase(String nom);

    List<Professeur> findByPrenomContainingIgnoreCase(String prenom);

    List<Professeur> findBySpecialiteContainingIgnoreCase(String specialite);
}