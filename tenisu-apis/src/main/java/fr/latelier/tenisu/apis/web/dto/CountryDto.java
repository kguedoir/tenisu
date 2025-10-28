package fr.latelier.tenisu.apis.web.dto;

import static fr.latelier.tenisu.apis.common.NamingConstants.CODE;
import static fr.latelier.tenisu.apis.common.NamingConstants.PICTURE;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.latelier.tenisu.apis.domain.Country;

/**
 * DTO représentant un pays (code ISO3 et URL de l'image/du drapeau).
 */
public record CountryDto(@JsonProperty(CODE) String code, @JsonProperty(PICTURE) String picture) {

    /**
     * Convertit une entité Country en CountryDto.
     * @param c entité JPA Country
     * @return le DTO correspondant, ou null si c’est null
     */
    public static CountryDto fromEntity(Country c) {
        if (c == null) return null;
        return new CountryDto(c.getCode(), c.getPicture());
    }
}
