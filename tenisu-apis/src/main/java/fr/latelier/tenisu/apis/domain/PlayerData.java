package fr.latelier.tenisu.apis.domain;

import static fr.latelier.tenisu.apis.common.NamingConstants.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Composant embarqué JPA contenant les données chiffrées d'un joueur.
 */
@Embeddable
@Getter
@NoArgsConstructor
public class PlayerData {

    @Column(name = RANK, nullable = false)
    private Integer rank;

    @Column(name = POINTS, nullable = false)
    private Integer points;

    @Column(name = WEIGHT, nullable = false)
    private Integer weight; // en grammes (selon le jeu de données)

    @Column(name = HEIGHT, nullable = false)
    private Integer height; // en centimètres

    @Column(name = AGE, nullable = false)
    private Integer age;

    /**
     * Constructeur complet pour initialiser les données d'un joueur.
     */
    public PlayerData(Integer rank, Integer points, Integer weight, Integer height, Integer age) {
        this.rank = rank;
        this.points = points;
        this.weight = weight;
        this.height = height;
        this.age = age;
    }
}
