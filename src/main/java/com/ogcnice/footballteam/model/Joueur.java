package com.ogcnice.footballteam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Entité représentant un joueur de football.
 * Un joueur appartient à une seule équipe.
 */
@Entity
@Table(name = "joueur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Joueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du joueur est obligatoire")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "La position du joueur est obligatoire")
    @Column(nullable = false)
    private String position;

    /**
     * Relation ManyToOne avec l'équipe.
     * JsonBackReference évite la boucle infinie lors de la sérialisation JSON.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id")
    @JsonBackReference
    private Equipe equipe;
}