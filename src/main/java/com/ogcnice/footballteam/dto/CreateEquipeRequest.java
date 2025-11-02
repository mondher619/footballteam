package com.ogcnice.footballteam.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour la création d'une équipe.
 * La liste de joueurs est optionnelle.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEquipeRequest {

    @NotBlank(message = "Le nom de l'équipe est obligatoire")
    private String name;

    @NotBlank(message = "L'acronyme de l'équipe est obligatoire")
    private String acronym;

    @NotNull(message = "Le budget est obligatoire")
    @Positive(message = "Le budget doit être positif")
    private BigDecimal budget;

    /**
     * Liste optionnelle de joueurs à créer avec l'équipe.
     */
    @Valid
    private List<CreateJoueurRequest> joueurs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateJoueurRequest {

        @NotBlank(message = "Le nom du joueur est obligatoire")
        private String name;

        @NotBlank(message = "La position du joueur est obligatoire")
        private String position;
    }
}