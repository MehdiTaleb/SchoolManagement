package net.mehdi.schoolmanagement.model;

public enum TypeNote {

    CONTROLE("Contrôle"),
    EXAMEN("Examen"),
    TP("Travaux Pratiques"),
    ORAL("Oral"),
    DEVOIR("Devoir");

    private final String label;

    TypeNote(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}