package net.mehdi.schoolmanagement.service;

import net.mehdi.schoolmanagement.model.Matiere;
import net.mehdi.schoolmanagement.model.Professeur;
import net.mehdi.schoolmanagement.repository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatiereService {

    @Autowired
    private MatiereRepository matiereRepository;

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Optional<Matiere> getMatiereById(Long id) {
        return matiereRepository.findById(id);
    }

    public Matiere addMatiere(Matiere matiere) {

        if (matiereRepository.findByNom(matiere.getNom()).isPresent()) {
            throw new IllegalArgumentException(
                    "Cette matière existe déjà"
            );
        }

        return matiereRepository.save(matiere);
    }

    public Matiere updateMatiere(Matiere matiere) {

        if (!matiereRepository.existsById(matiere.getId())) {
            throw new IllegalArgumentException(
                    "Matière non trouvée"
            );
        }

        return matiereRepository.save(matiere);
    }

    public void deleteMatiere(Long id) {

        if (!matiereRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Matière non trouvée"
            );
        }

        matiereRepository.deleteById(id);
    }

    public List<Matiere> searchByNom(String nom) {
        return matiereRepository
                .findByNomContainingIgnoreCase(nom);
    }

    public List<Matiere> getMatieresByProfesseur(Long professeurId) {
        return matiereRepository.findByProfesseurId(professeurId);
    }

    public void affecterProfesseur(Long matiereId, Professeur professeur) {

        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Matière non trouvée"));

        matiere.setProfesseur(professeur);

        matiereRepository.save(matiere);
    }
}