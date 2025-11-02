package com.ogcnice.footballteam.repository;

import com.ogcnice.footballteam.model.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Joueur.
 */
@Repository
public interface JoueurRepository extends JpaRepository<Joueur, Long> {
}