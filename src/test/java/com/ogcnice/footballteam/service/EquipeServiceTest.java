package com.ogcnice.footballteam.service;

import com.ogcnice.footballteam.dto.CreateEquipeRequest;
import com.ogcnice.footballteam.exception.EquipeAlreadyExistsException;
import com.ogcnice.footballteam.model.Equipe;
import com.ogcnice.footballteam.repository.EquipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour EquipeService.
 * Utilise Mockito pour simuler le repository.
 */
@ExtendWith(MockitoExtension.class)
class EquipeServiceTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeService equipeService;

    private CreateEquipeRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = CreateEquipeRequest.builder()
                .name("OGC Nice")
                .acronym("OGCN")
                .budget(new BigDecimal("50000000"))
                .joueurs(new ArrayList<>())
                .build();
    }

    @Test
    void testGetAllEquipes_Success() {
        // Arrange
        List<Equipe> equipes = List.of(
                Equipe.builder().id(1L).name("OGC Nice").acronym("OGCN").budget(new BigDecimal("50000000")).build(),
                Equipe.builder().id(2L).name("AS Monaco").acronym("ASM").budget(new BigDecimal("100000000")).build()
        );
        Page<Equipe> page = new PageImpl<>(equipes);
        Pageable pageable = PageRequest.of(0, 10);

        when(equipeRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Equipe> result = equipeService.getAllEquipes(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(equipeRepository, times(1)).findAll(pageable);
    }

    @Test
    void testCreateEquipe_Success() {
        // Arrange
        when(equipeRepository.existsByAcronym("OGCN")).thenReturn(false);
        when(equipeRepository.save(any(Equipe.class))).thenAnswer(invocation -> {
            Equipe equipe = invocation.getArgument(0);
            equipe.setId(1L);
            return equipe;
        });

        // Act
        Equipe result = equipeService.createEquipe(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals("OGC Nice", result.getName());
        assertEquals("OGCN", result.getAcronym());
        assertEquals(new BigDecimal("50000000"), result.getBudget());
        verify(equipeRepository, times(1)).existsByAcronym("OGCN");
        verify(equipeRepository, times(1)).save(any(Equipe.class));
    }

    @Test
    void testCreateEquipe_WithJoueurs_Success() {
        // Arrange
        List<CreateEquipeRequest.CreateJoueurRequest> joueurs = List.of(
                CreateEquipeRequest.CreateJoueurRequest.builder()
                        .name("Kasper Schmeichel")
                        .position("Gardien")
                        .build(),
                CreateEquipeRequest.CreateJoueurRequest.builder()
                        .name("Jean-Clair Todibo")
                        .position("Défenseur")
                        .build()
        );
        validRequest.setJoueurs(joueurs);

        when(equipeRepository.existsByAcronym("OGCN")).thenReturn(false);
        when(equipeRepository.save(any(Equipe.class))).thenAnswer(invocation -> {
            Equipe equipe = invocation.getArgument(0);
            equipe.setId(1L);
            return equipe;
        });

        // Act
        Equipe result = equipeService.createEquipe(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getJoueurs().size());
        assertEquals("Kasper Schmeichel", result.getJoueurs().get(0).getName());
        assertEquals("Gardien", result.getJoueurs().get(0).getPosition());
    }

    @Test
    void testCreateEquipe_DuplicateAcronym_ThrowsException() {
        // Arrange
        when(equipeRepository.existsByAcronym("OGCN")).thenReturn(true);

        // Act & Assert
        assertThrows(EquipeAlreadyExistsException.class, () -> {
            equipeService.createEquipe(validRequest);
        });

        verify(equipeRepository, times(1)).existsByAcronym("OGCN");
        verify(equipeRepository, never()).save(any(Equipe.class));
    }
}