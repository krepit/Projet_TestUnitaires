package com.ligue.pokemon.repository;

import com.ligue.pokemon.model.Dresseur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.jpa.defer-datasource-initialization=true",
        "spring.sql.init.encoding=UTF-8"
})
@AutoConfigureTestDatabase
class DresseurRepositoryTest {

    @Autowired
    private DresseurRepository dresseurRepository;

    @Test
    void should_return_dresseur_with_pokemons_when_id_exists() {
        // Arrange
        Long id = 1L;

        // Act
        Optional<Dresseur> result = dresseurRepository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Sacha", result.get().getNom());
        assertEquals("Kanto", result.get().getRegion());
        assertEquals(3, result.get().getPokemons().size());
    }

    @Test
    void should_return_empty_when_dresseur_does_not_exist() {
        // Arrange
        Long id = 999L;

        // Act
        Optional<Dresseur> result = dresseurRepository.findById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void should_return_dresseur_without_pokemon_when_he_owns_none() {
        // Arrange
        Long id = 3L;

        // Act
        Dresseur result = dresseurRepository.findById(id).orElseThrow();

        // Assert
        assertEquals(0, result.getPokemons().size());
    }

    @Test
    void should_return_dresseurs_of_region_when_searching_by_region() {
        // Arrange
        String region = "Kanto";

        // Act
        List<Dresseur> result = dresseurRepository.findByRegion(region);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void should_persist_dresseur_when_saving_new_one() {
        // Arrange
        Dresseur nouveau = new Dresseur(null, "Jeanne", "Sinnoh");

        // Act
        Dresseur saved = dresseurRepository.save(nouveau);

        // Assert
        assertEquals(4, dresseurRepository.count());
        assertEquals("Jeanne", dresseurRepository.findById(saved.getId()).orElseThrow().getNom());
    }
}
