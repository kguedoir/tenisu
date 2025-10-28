package fr.latelier.tenisu.apis.web.dto;

import static fr.latelier.tenisu.apis.common.NamingConstants.*;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO des statistiques agrégées:
 * - meilleur pays par ratio de victoires
 * - IMC moyen
 * - taille médiane (cm)
 */
public record StatsDto(
        @JsonProperty(BEST_COUNTRY) CountryWinRatioDto bestCountry,
        @JsonProperty(AVERAGE_BMI) double averageBmi,
        @JsonProperty(MEDIAN_HEIGHT_CM) double medianHeightCm) {

}
