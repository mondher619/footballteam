package com.ogcnice.footballteam.exception;

/**
 * Exception levée lorsqu'une ressource (équipe, joueur) n'est pas trouvée.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}