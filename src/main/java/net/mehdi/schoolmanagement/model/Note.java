package net.mehdi.schoolmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "note")
@Getter @Setter @ToString
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valeur;

    private LocalDate dateEvaluation;

    @Enumerated(EnumType.STRING)
    private TypeNote type;   // CONTROLE, EXAMEN, TP...

    // ============================================
    // MANY TO ONE → Student
    // ============================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    // ============================================
    // MANY TO ONE → Matiere
    // ============================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id", nullable = false)
    @ToString.Exclude
    private Matiere matiere;

    private LocalDateTime createdAt = LocalDateTime.now();
}