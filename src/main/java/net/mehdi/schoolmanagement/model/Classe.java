package net.mehdi.schoolmanagement.model;



import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@AllArgsConstructor @Getter @Setter @ToString
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message = "Le nom est obligatoire")
    @Column(unique = true)
    private String nom;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    private Integer nombreEtudiants;
    private String salle;

    @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructeurs
    public Classe() {}

    public Classe(String nom, Niveau niveau) {
        this.nom = nom;
        this.niveau = niveau;
    }


}

// Enum Niveau
enum Niveau {
    PRIMAIRE("Primaire"),
    SECONDAIRE("Secondaire"),
    SUPERIEUR("Supérieur");

    private final String label;

    Niveau(String label) { this.label = label; }

    public String getLabel() { return label; }
}