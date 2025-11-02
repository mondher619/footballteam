package com.ogcnice.footballteam.repository;

import com.ogcnice.footballteam.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Equipe.
 * Spring Data JPA génère automatiquement l'implémentation.
 */
@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    /**
     * Vérifie si une équipe existe avec cet acronyme.
     * Utile pour éviter les doublons lors de la création.
     */
    boolean existsByAcronym(String acronym);

    /**
     * Trouve une équipe par son acronyme.
     */
    Optional<Equipe> findByAcronym(String acronym);
}