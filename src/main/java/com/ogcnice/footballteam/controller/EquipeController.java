package com.ogcnice.footballteam.controller;

import com.ogcnice.footballteam.dto.CreateEquipeRequest;
import com.ogcnice.footballteam.dto.TransferJoueurRequest;
import com.ogcnice.footballteam.dto.TransferJoueurResponse;
import com.ogcnice.footballteam.model.Equipe;
import com.ogcnice.footballteam.service.EquipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des équipes de football.
 *
 * Endpoints disponibles:
 * - GET /api/equipes : Liste paginée et triée des équipes
 * - POST /api/equipes : Création d'une nouvelle équipe
 * - POST /api/equipes/transfer : Transfert d'un joueur
 */
@RestController
@RequestMapping("/api/equipes")
@RequiredArgsConstructor
@Slf4j
public class EquipeController {

    private final EquipeService equipeService;

    /**
     * Récupère la liste paginée des équipes.
     *
     * @param page numéro de la page (défaut: 0)
     * @param size taille de la page (défaut: 10)
     * @param sortBy champ de tri (défaut: name). Options: name, acronym, budget
     * @param sortDir direction du tri (défaut: asc). Options: asc, desc
     * @return Page d'équipes avec leurs joueurs
     *
     * Exemples d'utilisation:
     * - GET /api/equipes
     * - GET /api/equipes?page=0&size=5
     * - GET /api/equipes?sortBy=budget&sortDir=desc
     * - GET /api/equipes?page=1&size=10&sortBy=acronym&sortDir=asc
     */
    @GetMapping
    public ResponseEntity<Page<Equipe>> getAllEquipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("GET /api/equipes - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        // Validation du champ de tri
        if (!sortBy.equals("name") && !sortBy.equals("acronym") && !sortBy.equals("budget")) {
            log.warn("Champ de tri invalide: {}. Utilisation de 'name' par défaut", sortBy);
            sortBy = "name";
        }

        // Création de l'objet Sort
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Création du Pageable
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Equipe> equipes = equipeService.getAllEquipes(pageable);

        log.info("Retour de {} équipes sur {} au total",
                equipes.getNumberOfElements(),
                equipes.getTotalElements());

        return ResponseEntity.ok(equipes);
    }

    /**
     * Crée une nouvelle équipe avec ou sans joueurs.
     *
     * @param request données de l'équipe à créer
     * @return l'équipe créée avec statut 201 (CREATED)
     *
     * Exemple de requête JSON:
     * {
     *   "name": "OGC Nice",
     *   "acronym": "OGCN",
     *   "budget": 50000000,
     *   "joueurs": [
     *     {"name": "Kasper Schmeichel", "position": "Gardien"},
     *     {"name": "Jean-Clair Todibo", "position": "Défenseur"}
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<Equipe> createEquipe(@Valid @RequestBody CreateEquipeRequest request) {
        log.info("POST /api/equipes - Création de l'équipe: {}", request.getAcronym());

        Equipe createdEquipe = equipeService.createEquipe(request);

        log.info("Équipe créée avec succès - ID: {}", createdEquipe.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEquipe);
    }

    /**
     * Transfère un joueur d'une équipe à une autre.
     *
     * @param request données du transfert (joueurId, nouvelleEquipeId)
     * @return message de confirmation style Fabrizio Romano
     *
     * Exemple de requête JSON:
     * {
     *   "joueurId": 1,
     *   "nouvelleEquipeId": 2
     * }
     *
     * Exemple de réponse:
     * {
     *   "message": "🚨 HERE WE GO! Kasper Schmeichel (Gardien) has officially joined Paris Saint-Germain from OGC Nice! Deal confirmed and sealed. ✅🔴🔵",
     *   "joueurName": "Kasper Schmeichel",
     *   "position": "Gardien",
     *   "ancienneEquipe": "OGC Nice",
     *   "nouvelleEquipe": "Paris Saint-Germain",
     *   "confirmed": true
     * }
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransferJoueurResponse> transferJoueur(
            @Valid @RequestBody TransferJoueurRequest request) {

        log.info("POST /api/equipes/transfer - Transfert du joueur ID: {} vers équipe ID: {}",
                request.getJoueurId(),
                request.getNouvelleEquipeId());

        TransferJoueurResponse response = equipeService.transferJoueur(request);

        log.info("Transfert réussi: {}", response.getMessage());

        return ResponseEntity.ok(response);
    }
}