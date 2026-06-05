package com.autooglasi.entity;

/** Vrsta goriva za vozilo. */
public enum FuelType {
    BENZIN("Benzin"),
    DIZEL("Dizel"),
    ELEKTRICNI("Električni"),
    HIBRID("Hibrid"),
    PLIN("Plin (LPG)");

    private final String displayName;

    FuelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
