package com.ogcnice.footballteam.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO pour la demande de transfert d'un joueur.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferJoueurRequest {

    @NotNull(message = "L'ID du joueur est obligatoire")
    private Long joueurId;

    @NotNull(message = "L'ID de la nouvelle équipe est obligatoire")
    private Long nouvelleEquipeId;
}