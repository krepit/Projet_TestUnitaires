package com.ligue.pokemon.service;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.model.Dresseur;
import com.ligue.pokemon.model.Pokemon;
import com.ligue.pokemon.repository.DresseurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    @Mock
    private DresseurRepository dresseurRepository;

    @InjectMocks
    private DresseurService dresseurService;

    private Dresseur dresseurAvec(Pokemon... pokemons) {
        Dresseur dresseur = new Dresseur(1L, "Sacha", "Kanto");
        dresseur.setPokemons(List.of(pokemons));
        return dresseur;
    }

    private Pokemon pokemon(String type, int niveau) {
        return new Pokemon(null, "P", type, niveau, null);
    }

    @Test
    void should_return_sum_of_levels_when_dresseur_has_standard_pokemons() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 10), pokemon("Eau", 20));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30, niveau);
    }

    @Test
    void should_return_one_when_dresseur_has_no_pokemon() {
        // Arrange
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseurAvec()));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(1, niveau);
    }

    @Test
    void should_apply_diversity_bonus_when_dresseur_has_3_different_types() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 10), pokemon("Eau", 10), pokemon("Électrik", 10));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30 + 20, niveau);
    }

    @Test
    void should_not_apply_diversity_bonus_when_dresseur_has_only_2_different_types() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 10), pokemon("Feu", 10), pokemon("Eau", 10));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30, niveau);
    }

    @Test
    void should_apply_elite_bonus_when_pokemon_level_is_at_least_50() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 50), pokemon("Feu", 49));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(99 + 10, niveau);
    }

    @Test
    void should_cumulate_all_bonuses_when_dresseur_has_diversity_and_elite_pokemons() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 60), pokemon("Eau", 50), pokemon("Électrik", 10));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(120 + 20 + 2 * 10, niveau);
    }

    @Test
    void should_throw_exception_when_dresseur_does_not_exist() {
        // Arrange
        when(dresseurRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> dresseurService.calculerNiveauDresseur(99L));

        // Assert
        assertTrue(exception.getMessage().contains("99"));
    }

    @Test
    void should_return_dto_with_computed_level_when_dresseur_exists() {
        // Arrange
        Dresseur dresseur = dresseurAvec(pokemon("Feu", 55), pokemon("Eau", 5));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        Optional<DresseurDto> dto = dresseurService.getDresseurDto(1L);

        // Assert
        assertTrue(dto.isPresent());
        assertEquals("Sacha", dto.get().nomDresseur());
        assertEquals(2, dto.get().nombrePokemons());
        assertEquals(70, dto.get().niveauDresseur());
    }

    @Test
    void should_return_empty_dto_when_dresseur_does_not_exist() {
        // Arrange
        when(dresseurRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<DresseurDto> dto = dresseurService.getDresseurDto(99L);

        // Assert
        assertTrue(dto.isEmpty());
    }
}
