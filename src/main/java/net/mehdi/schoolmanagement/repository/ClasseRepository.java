package net.mehdi.schoolmanagement.repository;



import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    Optional<Classe> findByNom(String nom);

    List<Classe> findByNiveau(Niveau niveau);

    @Query("SELECT c FROM Classe c WHERE SIZE(c.students) > 0")
    List<Classe> findClassesActives();
}