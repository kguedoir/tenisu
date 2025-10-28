package fr.latelier.tenisu.apis.web.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.latelier.tenisu.apis.domain.Country;
import fr.latelier.tenisu.apis.domain.Player;
import fr.latelier.tenisu.apis.domain.PlayerData;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires des DTOs: mapping et immutabilité.
 */
class DtoMappingTest {

    @Test
    @DisplayName("PlayerDataDto: la liste 'lastResults' est copiée (copie défensive)")
    void playerDataDto_defensiveCopy() {
        // Arrange
        List<Integer> last = new ArrayList<>(List.of(1, 0, 1));
        PlayerDataDto dto = new PlayerDataDto(1, 1000, 80000, 190, 30, last);

        // Act: on modifie la liste source
        last.add(0);

        // Assert: le DTO n'est pas affecté
        assertThat(dto.lastResults()).containsExactly(1, 0, 1);
    }

    @Test
    @DisplayName("PlayerDto.fromEntity: mappe correctement les champs principaux")
    void playerDto_fromEntity_mapsFields() {
        // Arrange: construire une entité Player simple
        Player p = new PlayerBuilder()
                .id(42L)
                .firstname("Rafael")
                .lastname("Nadal")
                .shortname("R.NAD")
                .sex("M")
                .country(new Country("ESP", "pic-es"))
                .picture("pic")
                .data( new PlayerDataBuilder().rank(1).points(10000).weight(85000).height(185).age(36).build() )
                .last(List.of(1,0,1,1,0))
                .build();

        // Act
        PlayerDto dto = PlayerDto.fromEntity(p);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(42L);
        assertThat(dto.firstname()).isEqualTo("Rafael");
        assertThat(dto.country().code()).isEqualTo("ESP");
        assertThat(dto.data().lastResults()).containsExactly(1,0,1,1,0);
    }

    // Petits builders utilitaires internes pour créer des entités sans Lombok setters
    private static class PlayerBuilder {
        private final Player p = new Player();
        PlayerBuilder id(Long v){ setField(p, "id", v); return this; }
        PlayerBuilder firstname(String v){ setField(p, "firstname", v); return this; }
        PlayerBuilder lastname(String v){ setField(p, "lastname", v); return this; }
        PlayerBuilder shortname(String v){ setField(p, "shortname", v); return this; }
        PlayerBuilder sex(String v){ setField(p, "sex", v); return this; }
        PlayerBuilder country(Country v){ setField(p, "country", v); return this; }
        PlayerBuilder picture(String v){ setField(p, "picture", v); return this; }
        PlayerBuilder data(PlayerData v){ setField(p, "data", v); return this; }
        PlayerBuilder last(List<Integer> v){ setField(p, "last", v); return this; }
        Player build(){ return p; }
    }
    private static class PlayerDataBuilder {
        private final PlayerData d = new PlayerData();
        PlayerDataBuilder rank(Integer v){ setField(d, "rank", v); return this; }
        PlayerDataBuilder points(Integer v){ setField(d, "points", v); return this; }
        PlayerDataBuilder weight(Integer v){ setField(d, "weight", v); return this; }
        PlayerDataBuilder height(Integer v){ setField(d, "height", v); return this; }
        PlayerDataBuilder age(Integer v){ setField(d, "age", v); return this; }
        PlayerData build(){ return d; }
    }

    private static void setField(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
