package net.mehdi.schoolmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matieres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String code;

    private Integer coefficient;

    private Integer volumeHoraire;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    @ToString.Exclude
    private Professeur professeur;

    // ONE TO MANY → Notes
    // ============================================
    @OneToMany(mappedBy = "matiere", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Note> notes = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();


    public Matiere(String nom, String code, Integer coefficient) {
        this.nom = nom;
        this.code = code;
        this.coefficient = coefficient;
    }
}