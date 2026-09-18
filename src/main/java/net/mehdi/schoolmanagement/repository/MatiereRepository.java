package net.mehdi.schoolmanagement.repository;

import net.mehdi.schoolmanagement.model.Matiere;
import net.mehdi.schoolmanagement.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {

    Optional<Matiere> findByNom(String nom);

    Optional<Matiere> findByCode(String code);

    List<Matiere> findByNomContainingIgnoreCase(String nom);

    List<Matiere> findByProfesseur(Professeur professeur);

    List<Matiere> findByProfesseurId(Long professeurId);
}