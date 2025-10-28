package fr.latelier.tenisu.apis.web.dto;

import static fr.latelier.tenisu.apis.common.NamingConstants.*;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO représentant un pays et son ratio de victoires calculé.
 */
public record CountryWinRatioDto(
        @JsonProperty(CODE) String code,
        @JsonProperty(PICTURE) String picture,
        @JsonProperty(WIN_RATIO) double winRatio) {
}
