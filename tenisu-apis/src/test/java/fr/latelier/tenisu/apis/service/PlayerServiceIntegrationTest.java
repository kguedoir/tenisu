package fr.latelier.tenisu.apis.service;

import static org.assertj.core.api.Assertions.assertThat;

import fr.latelier.tenisu.apis.web.dto.CountryDto;
import fr.latelier.tenisu.apis.web.dto.PlayerDataDto;
import fr.latelier.tenisu.apis.web.dto.PlayerDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test d'intégration vérifiant que l'identifiant Player est auto-généré par la base (IDENTITY) et
 * que la création d'un pays inexistant fonctionne.
 */
@SpringBootTest
@Transactional
class PlayerServiceIntegrationTest {

    @Autowired
    private PlayerService playerService;

    @Test
    @DisplayName("create: générer un id auto-incrément et créer le pays si absent")
    void create_generates_id_and_country() {
        PlayerDto payload = new PlayerDto(null, "Test", "Player", "T.PLA", "M",
                new CountryDto("TST", "https://example.com/flag.png"), "https://example.com/pic.png",
                new PlayerDataDto(999, 12345, 70000, 180, 25, List.of(1,1,0,1,0)));

        PlayerDto created = playerService.create(payload);

        assertThat(created).isNotNull();
        assertThat(created.id()).as("L'id doit être généré").isNotNull();
        assertThat(created.country()).isNotNull();
        assertThat(created.country().code()).isEqualTo("TST");
    }
}

