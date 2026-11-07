package com.ligue.pokemon.repository;

import com.ligue.pokemon.model.Dresseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DresseurRepository extends JpaRepository<Dresseur, Long> {

    List<Dresseur> findByRegion(String region);
}
