package net.mehdi.schoolmanagement.service;



import net.mehdi.schoolmanagement.model.Classe;
import net.mehdi.schoolmanagement.model.Niveau;
import net.mehdi.schoolmanagement.model.Student;
import net.mehdi.schoolmanagement.repository.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClasseService {

    @Autowired
    private ClasseRepository classeRepository;

    // CRUD
    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    public Optional<Classe> getClasseById(Long id) {
        return classeRepository.findById(id);
    }

    public Classe addClasse(Classe classe) {
        if (classeRepository.findByNom(classe.getNom()).isPresent()) {
            throw new IllegalArgumentException("Cette classe existe déjà");
        }
        return classeRepository.save(classe);
    }

    public Classe updateClasse(Classe classe) {
        if (!classeRepository.existsById(classe.getId())) {
            throw new IllegalArgumentException("Classe non trouvée");
        }
        return classeRepository.save(classe);
    }

    public void deleteClasse(Long id) {
        if (!classeRepository.existsById(id)) {
            throw new IllegalArgumentException("Classe non trouvée");
        }
        classeRepository.deleteById(id);
    }

    // Recherche
    public List<Classe> getClassesByNiveau(Niveau niveau) {
        return classeRepository.findByNiveau(niveau);
    }

    // Gestion des étudiants
    public void addStudentToClasse(Long classeId, Student student) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new IllegalArgumentException("Classe non trouvée"));
        student.setClasse(classe);
        classe.getStudents().add(student);
        classe.setNombreEtudiants(classe.getStudents().size());
        classeRepository.save(classe);
    }

    public List<Student> getStudentsByClasse(Long classeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new IllegalArgumentException("Classe non trouvée"));
        return classe.getStudents();
    }

    // Statistiques
    public Integer countStudentsByClasse(Long classeId) {
        return getStudentsByClasse(classeId).size();
    }

    public Double getMoyenneClasse(Long classeId) {
        List<Student> students = getStudentsByClasse(classeId);
        if (students.isEmpty()) return 0.0;

        return students.stream()
                .mapToDouble(Student::getAverage)
                .average()
                .orElse(0.0);
    }
}