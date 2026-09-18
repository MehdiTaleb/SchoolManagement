package net.mehdi.schoolmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public Matiere(String nom, String code, Integer coefficient) {
        this.nom = nom;
        this.code = code;
        this.coefficient = coefficient;
    }
}