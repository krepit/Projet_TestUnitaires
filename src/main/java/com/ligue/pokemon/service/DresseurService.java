package com.ligue.pokemon.service;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.model.Dresseur;
import com.ligue.pokemon.model.Pokemon;
import com.ligue.pokemon.repository.DresseurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DresseurService {

    static final int NIVEAU_MINIMUM = 1;
    static final int SEUIL_DIVERSITE = 3;
    static final int BONUS_DIVERSITE = 20;
    static final int SEUIL_ELITE = 50;
    static final int BONUS_ELITE = 10;

    private final DresseurRepository dresseurRepository;

    public DresseurService(DresseurRepository dresseurRepository) {
        this.dresseurRepository = dresseurRepository;
    }

    // Récupérer un DresseurDto par l'ID du dresseur
    public Optional<DresseurDto> getDresseurDto(Long id) {
        return dresseurRepository.findById(id).map(dresseur -> new DresseurDto(
                dresseur.getNom(),
                dresseur.getPokemons().size(),
                calculerNiveau(dresseur)));
    }

    // Calculer le niveau d'un dresseur par son ID
    public int calculerNiveauDresseur(Long dresseurId) {
        Dresseur dresseur = dresseurRepository.findById(dresseurId)
                .orElseThrow(() -> new IllegalArgumentException("Dresseur introuvable : " + dresseurId));
        return calculerNiveau(dresseur);
    }

    // Calcul du niveau du dresseur en fonction de ses pokémons
    private int calculerNiveau(Dresseur dresseur) {
        List<Pokemon> pokemons = dresseur.getPokemons();
        if (pokemons == null || pokemons.isEmpty()) {
            return NIVEAU_MINIMUM;
        }

        int base = pokemons.stream().mapToInt(Pokemon::getNiveau).sum();
        long typesDistincts = pokemons.stream().map(Pokemon::getType).distinct().count();
        int bonusDiversite = typesDistincts >= SEUIL_DIVERSITE ? BONUS_DIVERSITE : 0;
        long nbElites = pokemons.stream().filter(p -> p.getNiveau() >= SEUIL_ELITE).count();

        return base + bonusDiversite + (int) nbElites * BONUS_ELITE;
    }
}
