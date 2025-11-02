package com.ogcnice.footballteam.dto;

import lombok.*;

/**
 * DTO pour la réponse d'un transfert avec message style Fabrizio Romano.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferJoueurResponse {

    private String message;
    private String joueurName;
    private String position;
    private String ancienneEquipe;
    private String nouvelleEquipe;
    private Boolean confirmed;

    /**
     * Génère un message style Fabrizio Romano : "Here we go!"
     */
    public static TransferJoueurResponse createFabrizioStyle(
            String joueurName,
            String position,
            String ancienneEquipe,
            String nouvelleEquipe) {

        String message = String.format(
                "🚨 HERE WE GO! %s (%s) has officially joined %s from %s! " +
                        "Deal confirmed and sealed. ✅🔴🔵 #TransferNews #HereWeGo",
                joueurName,
                position,
                nouvelleEquipe,
                ancienneEquipe
        );

        return TransferJoueurResponse.builder()
                .message(message)
                .joueurName(joueurName)
                .position(position)
                .ancienneEquipe(ancienneEquipe)
                .nouvelleEquipe(nouvelleEquipe)
                .confirmed(true)
                .build();
    }
}