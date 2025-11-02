package com.ogcnice.footballteam.exception;

/**
 * Exception levée lorsqu'on tente de créer une équipe
 * dont l'acronyme existe déjà.
 */
public class EquipeAlreadyExistsException extends RuntimeException {

    public EquipeAlreadyExistsException(String message) {
        super(message);
    }
}