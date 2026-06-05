package com.autooglasi.entity;

/** Status oglasa. */
public enum AdStatus {
    AKTIVAN("Aktivan"),
    PRODAT("Prodat");

    private final String displayName;

    AdStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
