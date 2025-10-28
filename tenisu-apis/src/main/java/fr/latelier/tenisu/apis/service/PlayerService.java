package fr.latelier.tenisu.apis.service;

import fr.latelier.tenisu.apis.web.dto.PlayerDto;
import fr.latelier.tenisu.apis.web.dto.StatsDto;
import java.util.List;

/**
 * Service applicatif pour les cas d'utilisation liés aux joueurs.
 *
 * Responsabilités :
 * - Encapsule la recherche et la récupération des joueurs.
 * - Réalise une normalisation/validation basique des entrées (ex. trim du texte de recherche).
 *
 * Retourne des DTOs côté web. À l'avenir, on pourra adapter le mapping selon les besoins.
 */
public interface PlayerService {

    /**
     * Recherche des joueurs par nom. Correspondances insensibles à la casse sur
     * le prénom, le nom et le surnom.
     *
     * @param name saisie brute de l'utilisateur; peut être null/vide
     * @return liste non nulle de joueurs. Retourne une liste vide si l'entrée est null/vide ou si aucun résultat n'est trouvé.
     */
    List<PlayerDto> searchByName(String name);

    /**
     * Trouver un joueur par identifiant.
     *
     * @param id identifiant du joueur
     * @return le joueur s'il est trouvé, sinon lève une NotFoundException
     */
    PlayerDto getById(Long id);

    /**
     * Vérifie l'existence d'un joueur par identifiant.
     *
     * @param id identifiant du joueur
     * @return true si le joueur existe, false sinon
     */
    boolean exists(Long id);

    /**
     * Créer un nouveau joueur.
     *
     * Remarque: dans cette version, la persistance peut être simulée et évoluer ultérieurement.
     *
     * @param player joueur à créer (payload JSON)
     * @return le joueur créé (avec son identifiant)
     */
    PlayerDto create(PlayerDto player);

    /**
     * Calculer et retourner des statistiques agrégées sur tous les joueurs.
     * - Pays avec le meilleur ratio de victoires (d'après les derniers résultats)
     * - IMC moyen des joueurs
     * - Taille médiane (cm)
     */
    StatsDto getStats();
}
