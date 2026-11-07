# Pokémon Management API

## Stack

- Java 17, Spring Boot 3.3.5, Maven
- Spring Web, Spring Data JPA
- H2 en mémoire (défaut) ou MySQL (profil `mysql`)
- JUnit 5, Mockito, MockMvc

## Lancer le projet

```bash
mvn spring-boot:run
```

Console H2 : http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:pokemondb`, utilisateur `sa`).

Avec MySQL :

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Variables d'environnement : `DB_HOST` (localhost), `DB_PORT` (3306), `DB_NAME` (pokemondb), `DB_USER` (root), et le mot de passe de la base.

## Endpoint

`GET /dresseurs/{id}`

| Statut | Cas |
|--------|-----|
| 200 | Dresseur trouvé |
| 404 | Dresseur inexistant |
| 400 | `id` non numérique |

Exemple de réponse :

```json
{ "nomDresseur": "Sacha", "nombrePokemons": 3, "niveauDresseur": 105 }
```

## Règle de calcul du niveau d'un dresseur

- Somme des niveaux de ses Pokémon (1 s'il n'en a aucun)
- +20 s'il possède au moins 3 types distincts
- +10 par Pokémon de niveau 50 ou plus

## Architecture

```
Controller → Service → Repository → Model (Dresseur 1──N Pokemon)
```

Code dans `src/main/java/com/ligue/pokemon` : `controller`, `service`, `repository`, `model`, `dto`.

## Tests

```bash
mvn test
```

- `DresseurServiceTest` : logique métier (Mockito)
- `DresseurControllerTest` : `@WebMvcTest` + MockMvc
- `DresseurRepositoryTest` : `@DataJpaTest` avec `data.sql`

## CI

GitHub Actions (`.github/workflows/ci.yml`) exécute `mvn test` à chaque push et pull request.
