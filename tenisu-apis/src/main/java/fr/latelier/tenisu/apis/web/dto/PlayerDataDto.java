package fr.latelier.tenisu.apis.web.dto;

import static fr.latelier.tenisu.apis.common.NamingConstants.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.latelier.tenisu.apis.domain.PlayerData;
import java.util.List;

/**
 * DTO représentant les données chiffrées d'un joueur (classement, points, poids, taille, âge, derniers résultats).
 */
public record PlayerDataDto(
        @JsonProperty(RANK) Integer rank,
        @JsonProperty(POINTS) Integer points,
        @JsonProperty(WEIGHT) Integer weight,
        @JsonProperty(HEIGHT) Integer height,
        @JsonProperty(AGE) Integer age,
        @JsonProperty(LAST) List<Integer> lastResults) {

    /**
     * Constructeur défensif qui copie la liste des derniers résultats pour éviter les modifications externes.
     */
    public PlayerDataDto(Integer rank, Integer points, Integer weight, Integer height, Integer age, List<Integer> lastResults) {
        this.rank = rank;
        this.points = points;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.lastResults = lastResults == null ? List.of() : List.copyOf(lastResults);
    }

    /**
     * Mapper depuis l'entité PlayerData et la liste des derniers résultats.
     * @param d entité embarquée PlayerData
     * @param last derniers résultats (0/1)
     * @return un DTO ou null si d est null
     */
    public static PlayerDataDto fromEntity(PlayerData d, List<Integer> last) {
        if (d == null) return null;
        return new PlayerDataDto(d.getRank(), d.getPoints(), d.getWeight(), d.getHeight(), d.getAge(), last);
    }
}
