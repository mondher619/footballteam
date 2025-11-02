package com.ogcnice.footballteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogcnice.footballteam.dto.CreateEquipeRequest;
import com.ogcnice.footballteam.model.Equipe;
import com.ogcnice.footballteam.repository.EquipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour l'API Equipe.
 * Teste l'ensemble de la stack (Controller -> Service -> Repository -> Database).
 */
@SpringBootTest
@AutoConfigureMockMvc
class EquipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EquipeRepository equipeRepository;

    @BeforeEach
    void setUp() {
        // Nettoyage de la base avant chaque test
        equipeRepository.deleteAll();
    }

    @Test
    void testGetAllEquipes_EmptyDatabase() throws Exception {
        mockMvc.perform(get("/api/equipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void testGetAllEquipes_WithData() throws Exception {
        // Arrange - Création de données de test
        Equipe equipe1 = Equipe.builder()
                .name("OGC Nice")
                .acronym("OGCN")
                .budget(new BigDecimal("50000000"))
                .build();

        Equipe equipe2 = Equipe.builder()
                .name("AS Monaco")
                .acronym("ASM")
                .budget(new BigDecimal("100000000"))
                .build();

        equipeRepository.saveAll(List.of(equipe1, equipe2));

        // Act & Assert
        mockMvc.perform(get("/api/equipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].name").exists())
                .andExpect(jsonPath("$.content[0].acronym").exists())
                .andExpect(jsonPath("$.content[0].budget").exists());
    }

    @Test
    void testGetAllEquipes_WithPagination() throws Exception {
        // Arrange
        for (int i = 1; i <= 15; i++) {
            Equipe equipe = Equipe.builder()
                    .name("Equipe " + i)
                    .acronym("EQ" + i)
                    .budget(new BigDecimal(i * 1000000))
                    .build();
            equipeRepository.save(equipe);
        }

        // Act & Assert - Page 0, size 10
        mockMvc.perform(get("/api/equipes?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.number").value(0));

        // Page 1, size 10
        mockMvc.perform(get("/api/equipes?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.number").value(1));
    }

    @Test
    void testGetAllEquipes_WithSorting() throws Exception {
        // Arrange
        Equipe equipe1 = Equipe.builder()
                .name("Zebra FC")
                .acronym("ZFC")
                .budget(new BigDecimal("30000000"))
                .build();

        Equipe equipe2 = Equipe.builder()
                .name("Alpha FC")
                .acronym("AFC")
                .budget(new BigDecimal("50000000"))
                .build();

        equipeRepository.saveAll(List.of(equipe1, equipe2));

        // Act & Assert - Tri par nom ascendant
        mockMvc.perform(get("/api/equipes?sortBy=name&sortDir=asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Alpha FC"))
                .andExpect(jsonPath("$.content[1].name").value("Zebra FC"));

        // Tri par budget descendant
        mockMvc.perform(get("/api/equipes?sortBy=budget&sortDir=desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].budget").value(50000000))
                .andExpect(jsonPath("$.content[1].budget").value(30000000));
    }

    @Test
    void testCreateEquipe_Success() throws Exception {
        // Arrange
        CreateEquipeRequest request = CreateEquipeRequest.builder()
                .name("OGC Nice")
                .acronym("OGCN")
                .budget(new BigDecimal("50000000"))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("OGC Nice"))
                .andExpect(jsonPath("$.acronym").value("OGCN"))
                .andExpect(jsonPath("$.budget").value(50000000));
    }

    @Test
    void testCreateEquipe_WithJoueurs_Success() throws Exception {
        // Arrange
        CreateEquipeRequest.CreateJoueurRequest joueur1 =
                CreateEquipeRequest.CreateJoueurRequest.builder()
                        .name("Kasper Schmeichel")
                        .position("Gardien")
                        .build();

        CreateEquipeRequest.CreateJoueurRequest joueur2 =
                CreateEquipeRequest.CreateJoueurRequest.builder()
                        .name("Jean-Clair Todibo")
                        .position("Défenseur")
                        .build();

        CreateEquipeRequest request = CreateEquipeRequest.builder()
                .name("OGC Nice")
                .acronym("OGCN")
                .budget(new BigDecimal("50000000"))
                .joueurs(List.of(joueur1, joueur2))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.joueurs", hasSize(2)))
                .andExpect(jsonPath("$.joueurs[0].name").value("Kasper Schmeichel"))
                .andExpect(jsonPath("$.joueurs[0].position").value("Gardien"))
                .andExpect(jsonPath("$.joueurs[1].name").value("Jean-Clair Todibo"));
    }

    @Test
    void testCreateEquipe_ValidationError_MissingName() throws Exception {
        // Arrange
        CreateEquipeRequest request = CreateEquipeRequest.builder()
                .acronym("OGCN")
                .budget(new BigDecimal("50000000"))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void testCreateEquipe_DuplicateAcronym_ConflictError() throws Exception {
        // Arrange - Création d'une première équipe
        Equipe existing = Equipe.builder()
                .name("Existing Team")
                .acronym("OGCN")
                .budget(new BigDecimal("30000000"))
                .build();
        equipeRepository.save(existing);

        CreateEquipeRequest request = CreateEquipeRequest.builder()
                .name("OGC Nice")
                .acronym("OGCN")
                .budget(new BigDecimal("50000000"))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}