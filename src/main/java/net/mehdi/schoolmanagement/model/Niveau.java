package net.mehdi.schoolmanagement.model;

public enum Niveau {

    PRIMAIRE("Primaire"),
    SECONDAIRE("Secondaire"),
    SUPERIEUR("Supérieur");

    private final String label;

    Niveau(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}