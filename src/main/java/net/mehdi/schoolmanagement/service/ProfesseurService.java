package net.mehdi.schoolmanagement.service;

import net.mehdi.schoolmanagement.model.Professeur;
import net.mehdi.schoolmanagement.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfesseurService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    public List<Professeur> getAllProfesseurs() {
        return professeurRepository.findAll();
    }

    public Optional<Professeur> getProfesseurById(Long id) {
        return professeurRepository.findById(id);
    }

    public Professeur addProfesseur(Professeur professeur) {

        if (professeur.getEmail() != null &&
                professeurRepository.findByEmail(professeur.getEmail()).isPresent()) {

            throw new IllegalArgumentException(
                    "Un professeur avec cet email existe déjà"
            );
        }

        return professeurRepository.save(professeur);
    }

    public Professeur updateProfesseur(Professeur professeur) {

        if (!professeurRepository.existsById(professeur.getId())) {
            throw new IllegalArgumentException("Professeur non trouvé");
        }

        return professeurRepository.save(professeur);
    }

    public void deleteProfesseur(Long id) {

        if (!professeurRepository.existsById(id)) {
            throw new IllegalArgumentException("Professeur non trouvé");
        }

        professeurRepository.deleteById(id);
    }

    public List<Professeur> searchByNom(String nom) {
        return professeurRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Professeur> searchBySpecialite(String specialite) {
        return professeurRepository
                .findBySpecialiteContainingIgnoreCase(specialite);
    }
}