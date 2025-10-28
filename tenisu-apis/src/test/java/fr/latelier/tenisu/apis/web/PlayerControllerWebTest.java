package fr.latelier.tenisu.apis.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.latelier.tenisu.apis.common.NamingConstants;
import fr.latelier.tenisu.apis.service.PlayerService;
import fr.latelier.tenisu.apis.web.dto.CountryDto;
import fr.latelier.tenisu.apis.web.dto.PlayerDataDto;
import fr.latelier.tenisu.apis.web.dto.PlayerDto;
import fr.latelier.tenisu.apis.web.dto.StatsDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import fr.latelier.tenisu.apis.web.error.NotFoundException;

/**
 * Tests Web MVC du PlayerController avec un PlayerService mocké.
 */
@WebMvcTest(PlayerController.class)
class PlayerControllerWebTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    PlayerService playerService;

    private String asJson(String json) { return json; }

    @Test
    @DisplayName("GET /apis/players sans paramètre renvoie une liste JSON")
    void getPlayers_noParam_returnsList() throws Exception {
        // Arrange
        PlayerDto p = new PlayerDto(1L, "Novak", "Djokovic", "N.DJO", "M", new CountryDto("SRB", "pic"), "pic", new PlayerDataDto(1, 10000, 80000, 188, 36, List.of(1,1,0,1,1)));
        when(playerService.searchByName(null)).thenReturn(List.of(p));

        // Act + Assert
        mvc.perform(get(WebConstants.Path.PLAYERS_BASE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstname", is("Novak")));
    }

    @Test
    @DisplayName("GET /apis/players avec paramètre 'search' relaie la valeur vers le service")
    void getPlayers_withParam_relaysValue() throws Exception {
        // Arrange
        when(playerService.searchByName("Nadal")).thenReturn(List.of());

        // Act + Assert
        mvc.perform(get(WebConstants.Path.PLAYERS_BASE).param(NamingConstants.SEARCH, "Nadal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /apis/players/{id} renvoie 200 si trouvé, 404 sinon")
    void getPlayerById_foundAndNotFound() throws Exception {
        // Arrange
        when(playerService.getById(1L)).thenReturn(new PlayerDto(1L, "N", "D", "N.D", "M", new CountryDto("SRB", "pic"), "pic", null));
        when(playerService.getById(2L)).thenThrow(new NotFoundException("Player", "2"));

        // found
        mvc.perform(get(WebConstants.Path.PLAYERS_BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        // not found
        mvc.perform(get(WebConstants.Path.PLAYERS_BASE + "/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /apis/players/stats renvoie un objet StatsDto")
    void getStats_returnsStats() throws Exception {
        // Arrange
        when(playerService.getStats()).thenReturn(new StatsDto(null, 21.5, 185));

        // Act + Assert
        mvc.perform(get(WebConstants.Path.PLAYERS_BASE + WebConstants.Path.STATS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageBmi", is(21.5)))
                .andExpect(jsonPath("$.medianHeightCm", is(185.0)));
    }

    @Test
    @DisplayName("POST /apis/players crée un joueur et renvoie 201 avec Location")
    void postPlayers_createsAndReturns201() throws Exception {
        // Arrange
        String payload = "{\"id\":42,\"firstname\":\"Novak\",\"lastname\":\"Djokovic\",\"shortname\":\"N.DJO\",\"sex\":\"M\",\"country\":{\"code\":\"SRB\",\"picture\":\"pic-srb\"},\"picture\":\"pic\",\"data\":{\"rank\":1,\"points\":10000,\"weight\":80000,\"height\":188,\"age\":36,\"last\":[1,1,0,1,1]}}";
        PlayerDto created = new PlayerDto(42L, "Novak", "Djokovic", "N.DJO", "M",
                new CountryDto("SRB", "pic-srb"), "pic",
                new PlayerDataDto(1, 10000, 80000, 188, 36, List.of(1,1,0,1,1)));
        when(playerService.create(any(PlayerDto.class))).thenReturn(created);

        // Act + Assert
        mvc.perform(post(WebConstants.Path.PLAYERS_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(payload)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", WebConstants.Path.PLAYERS_BASE + "/42"))
                .andExpect(jsonPath("$.id", is(42)))
                .andExpect(jsonPath("$.firstname", is("Novak")));
    }
}
