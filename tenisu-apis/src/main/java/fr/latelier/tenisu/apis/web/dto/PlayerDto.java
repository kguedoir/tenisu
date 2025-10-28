package fr.latelier.tenisu.apis.web.dto;

import static fr.latelier.tenisu.apis.common.NamingConstants.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.latelier.tenisu.apis.domain.Player;
import java.util.List;

/**
 * DTO (Data Transfer Object) pour exposer les informations d'un joueur via la couche web.
 * Permet de stabiliser les charges utiles de l'API et de découpler des entités JPA.
 */
public record PlayerDto(
        @JsonProperty(ID) Long id,
        @JsonProperty(FIRSTNAME) String firstname,
        @JsonProperty(LASTNAME) String lastname,
        @JsonProperty(SHORTNAME) String shortname,
        @JsonProperty(SEX) String sex,
        @JsonProperty(COUNTRY) CountryDto country,
        @JsonProperty(PICTURE) String picture,
        @JsonProperty(DATA) PlayerDataDto data) {

    /**
     * Crée un PlayerDto à partir d'une entité Player.
     * @param p entité JPA Player
     * @return le DTO correspondant, ou null si p est null
     */
    public static PlayerDto fromEntity(Player p) {
        if (p == null) return null;
        List<Integer> last = p.getLast();
        return new PlayerDto(
                p.getId(),
                p.getFirstname(),
                p.getLastname(),
                p.getShortname(),
                p.getSex(),
                CountryDto.fromEntity(p.getCountry()),
                p.getPicture(),
                PlayerDataDto.fromEntity(p.getData(), last)
        );
    }
}
