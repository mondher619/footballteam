package com.ogcnice.footballteam.service;

import com.ogcnice.footballteam.dto.CreateEquipeRequest;
import com.ogcnice.footballteam.dto.TransferJoueurRequest;
import com.ogcnice.footballteam.dto.TransferJoueurResponse;
import com.ogcnice.footballteam.exception.EquipeAlreadyExistsException;
import com.ogcnice.footballteam.exception.ResourceNotFoundException;
import com.ogcnice.footballteam.model.Equipe;
import com.ogcnice.footballteam.model.Joueur;
import com.ogcnice.footballteam.repository.EquipeRepository;
import com.ogcnice.footballteam.repository.JoueurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service métier pour la gestion des équipes et transferts.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final JoueurRepository joueurRepository;

    /**
     * Récupère toutes les équipes avec pagination et tri.
     *
     * @param pageable configuration de la pagination et du tri
     * @return Page d'équipes
     */
    @Transactional(readOnly = true)
    public Page<Equipe> getAllEquipes(Pageable pageable) {
        log.debug("Récupération des équipes - page: {}, taille: {}, tri: {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        return equipeRepository.findAll(pageable);
    }

    /**
     * Crée une nouvelle équipe avec ou sans joueurs.
     *
     * @param request données de l'équipe à créer
     * @return l'équipe créée
     * @throws EquipeAlreadyExistsException si l'acronyme existe déjà
     */
    @Transactional
    public Equipe createEquipe(CreateEquipeRequest request) {
        log.info("Création d'une nouvelle équipe: {}", request.getAcronym());

        // Vérification de l'unicité de l'acronyme
        if (equipeRepository.existsByAcronym(request.getAcronym())) {
            log.error("L'équipe avec l'acronyme {} existe déjà", request.getAcronym());
            throw new EquipeAlreadyExistsException(
                    "Une équipe avec l'acronyme " + request.getAcronym() + " existe déjà"
            );
        }

        // Création de l'équipe
        Equipe equipe = Equipe.builder()
                .name(request.getName())
                .acronym(request.getAcronym())
                .budget(request.getBudget())
                .build();

        // Ajout des joueurs si présents
        if (request.getJoueurs() != null && !request.getJoueurs().isEmpty()) {
            log.debug("Ajout de {} joueurs à l'équipe", request.getJoueurs().size());

            request.getJoueurs().forEach(joueurRequest -> {
                Joueur joueur = Joueur.builder()
                        .name(joueurRequest.getName())
                        .position(joueurRequest.getPosition())
                        .build();
                equipe.addJoueur(joueur);
            });
        }

        Equipe savedEquipe = equipeRepository.save(equipe);
        log.info("Équipe créée avec succès - ID: {}, Acronyme: {}",
                savedEquipe.getId(),
                savedEquipe.getAcronym());

        return savedEquipe;
    }

    /**
     * Transfère un joueur d'une équipe à une autre.
     *
     * @param request données du transfert
     * @return réponse avec message style Fabrizio Romano
     * @throws ResourceNotFoundException si le joueur ou l'équipe n'existe pas
     */
    @Transactional
    public TransferJoueurResponse transferJoueur(TransferJoueurRequest request) {
        log.info("Transfert du joueur ID: {} vers l'équipe ID: {}",
                request.getJoueurId(),
                request.getNouvelleEquipeId());

        // Récupération du joueur
        Joueur joueur = joueurRepository.findById(request.getJoueurId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Joueur non trouvé avec l'ID: " + request.getJoueurId()
                ));

        // Récupération de la nouvelle équipe
        Equipe nouvelleEquipe = equipeRepository.findById(request.getNouvelleEquipeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Équipe non trouvée avec l'ID: " + request.getNouvelleEquipeId()
                ));

        // Sauvegarde de l'ancienne équipe pour le message
        String ancienneEquipeName = joueur.getEquipe() != null
                ? joueur.getEquipe().getName()
                : "Free Agent";

        // Retirer le joueur de l'ancienne équipe si elle existe
        if (joueur.getEquipe() != null) {
            joueur.getEquipe().removeJoueur(joueur);
        }

        // Ajouter le joueur à la nouvelle équipe
        nouvelleEquipe.addJoueur(joueur);

        // Sauvegarder les changements
        equipeRepository.save(nouvelleEquipe);

        log.info("✅ Transfert confirmé: {} de {} vers {}",
                joueur.getName(),
                ancienneEquipeName,
                nouvelleEquipe.getName());

        // Génération du message style Fabrizio Romano
        return TransferJoueurResponse.createFabrizioStyle(
                joueur.getName(),
                joueur.getPosition(),
                ancienneEquipeName,
                nouvelleEquipe.getName()
        );
    }
}