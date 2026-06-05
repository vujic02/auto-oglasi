package com.autooglasi.entity;

/** Tip menjača. */
public enum TransmissionType {
    MANUELNI("Manuelni"),
    AUTOMATSKI("Automatski"),
    POLUAUTOMATSKI("Poluautomatski");

    private final String displayName;

    TransmissionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
