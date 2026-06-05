package com.autooglasi.exception;

/** Baca se kada traženi entitet ne postoji. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
