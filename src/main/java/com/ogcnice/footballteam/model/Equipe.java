package com.ogcnice.footballteam.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une équipe de football.
 * Une équipe peut avoir plusieurs joueurs.
 */
@Entity
@Table(name = "equipe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'équipe est obligatoire")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "L'acronyme de l'équipe est obligatoire")
    @Column(nullable = false, unique = true)
    private String acronym;

    @NotNull(message = "Le budget est obligatoire")
    @Positive(message = "Le budget doit être positif")
    @Column(nullable = false)
    private BigDecimal budget;

    /**
     * Liste des joueurs de l'équipe.
     * CascadeType.ALL permet de persister/supprimer les joueurs avec l'équipe.
     * orphanRemoval=true supprime les joueurs qui ne sont plus associés à l'équipe.
     */
    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Joueur> joueurs = new ArrayList<>();

    /**
     * Méthode utilitaire pour ajouter un joueur à l'équipe.
     * Maintient la cohérence bidirectionnelle de la relation.
     */
    public void addJoueur(Joueur joueur) {
        joueurs.add(joueur);
        joueur.setEquipe(this);
    }

    /**
     * Méthode utilitaire pour retirer un joueur de l'équipe.
     */
    public void removeJoueur(Joueur joueur) {
        joueurs.remove(joueur);
        joueur.setEquipe(null);
    }
}