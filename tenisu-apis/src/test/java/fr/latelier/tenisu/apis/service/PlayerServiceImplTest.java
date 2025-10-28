package fr.latelier.tenisu.apis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import fr.latelier.tenisu.apis.domain.Country;
import fr.latelier.tenisu.apis.domain.Player;
import fr.latelier.tenisu.apis.domain.PlayerData;
import fr.latelier.tenisu.apis.repository.PlayerRepository;
import fr.latelier.tenisu.apis.service.impl.PlayerServiceImpl;
import fr.latelier.tenisu.apis.web.dto.*;
import java.util.List;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import fr.latelier.tenisu.apis.web.error.NotFoundException;

/**
 * Tests unitaires du service PlayerServiceImpl.
 *
 * Tous les commentaires sont en français conformément à la demande.
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PlayerServiceImpl service;

    @BeforeEach
    void setup() {
        // Rien à initialiser pour le moment
    }

    @Test
    @DisplayName("searchByName: quand l'entrée est null ou vide, on retourne tout trié par rang")
    void searchByName_nullOrEmpty_returnsAllOrdered() {
        // Arrange
        when(playerRepository.findAllByOrderByDataRankAsc()).thenReturn(List.of());

        // Act
        List<PlayerDto> res1 = service.searchByName(null);
        List<PlayerDto> res2 = service.searchByName("   ");

        // Assert
        verify(playerRepository, times(2)).findAllByOrderByDataRankAsc();
        assertThat(res1).isNotNull();
        assertThat(res2).isNotNull();
    }

    @Test
    @DisplayName("searchByName: quand l'entrée est fournie, on utilise la recherche insensible à la casse")
    void searchByName_withValue_usesRepositoryFinder() {
        // Arrange
        when(playerRepository.findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrShortnameContainingIgnoreCaseOrderByDataRankAsc(
                anyString(), anyString(), anyString())).thenReturn(List.of());

        // Act
        List<PlayerDto> res = service.searchByName("NaDjOkOvIc");

        // Assert
        verify(playerRepository).findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrShortnameContainingIgnoreCaseOrderByDataRankAsc(
                eq("NaDjOkOvIc"), eq("NaDjOkOvIc"), eq("NaDjOkOvIc"));
        assertThat(res).isNotNull();
    }

    @Test
    @DisplayName("getById: null ou introuvable -> NotFoundException; trouvé -> DTO")
    void getById_behaviour() {
        // Arrange
        Player player = mock(Player.class);
        when(playerRepository.findById(1L)).thenReturn(java.util.Optional.of(player));
        when(playerRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        // Act + Assert
        // null id
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.getById(null))
                .isInstanceOf(NotFoundException.class);

        // not found
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.getById(2L))
                .isInstanceOf(NotFoundException.class);

        // found
        PlayerDto found = service.getById(1L);
        assertThat(found).isNotNull();
    }

    @Test
    @DisplayName("getStats: calcul du meilleur pays, de l'IMC moyen et de la médiane de taille")
    void getStats_computation() {
        // Arrange: on construit 3 joueurs via mocks pour contrôler précisément les valeurs
        Player p1 = mock(Player.class);
        Player p2 = mock(Player.class);
        Player p3 = mock(Player.class);

        // Pays
        Country fr = mock(Country.class);
        when(fr.getCode()).thenReturn("FRA");
        when(fr.getPicture()).thenReturn("pic-fr");
        Country es = mock(Country.class);
        when(es.getCode()).thenReturn("ESP");
        when(es.getPicture()).thenReturn("pic-es");

        // Données physiques (poids en grammes, taille en cm)
        PlayerData d1 = mock(PlayerData.class);
        when(d1.getWeight()).thenReturn(80000); // 80kg
        when(d1.getHeight()).thenReturn(190);   // 1.90m
        PlayerData d2 = mock(PlayerData.class);
        when(d2.getWeight()).thenReturn(70000); // 70kg
        when(d2.getHeight()).thenReturn(180);   // 1.80m
        PlayerData d3 = mock(PlayerData.class);
        when(d3.getWeight()).thenReturn(0);     // invalide
        when(d3.getHeight()).thenReturn(0);     // invalide

        // Wiring des joueurs
        when(p1.getCountry()).thenReturn(fr);
        when(p1.getData()).thenReturn(d1);
        when(p1.getLast()).thenReturn(List.of(1, 1, 0, 1, 1)); // 4/5

        when(p2.getCountry()).thenReturn(fr);
        when(p2.getData()).thenReturn(d2);
        when(p2.getLast()).thenReturn(List.of(1, 0, 0, 1, 0)); // 2/5 → total FRA: 6/10 = 0.6

        when(p3.getCountry()).thenReturn(es);
        when(p3.getData()).thenReturn(d3);
        when(p3.getLast()).thenReturn(List.of(1, 1, 1, 1, 0)); // 4/5 → ESP: 0.8 (meilleur)

        when(playerRepository.findAllWithDetails()).thenReturn(List.of(p1, p2, p3));

        // Act
        StatsDto stats = service.getStats();

        // Assert
        assertThat(stats).isNotNull();
        CountryWinRatioDto best = stats.bestCountry();
        assertThat(best).isNotNull();
        assertThat(best.code()).isEqualTo("ESP");
        assertThat(best.winRatio()).isEqualTo(0.8d);

        // IMC moyen: p1=80/(1.9^2)=22.160..., p2=70/(1.8^2)=21.604..., p3 ignoré → moyenne ~21.882
        assertThat(stats.averageBmi()).isBetween(21.80, 21.95);

        // Médiane des tailles: [180,190] → médiane = (180+190)/2 = 185
        assertThat(stats.medianHeightCm()).isEqualTo(185.0);
    }

    @Test
    @DisplayName("create: persiste un joueur et renvoie le DTO créé")
    void create_persists_and_returns_dto() {
        // Arrange
        PlayerDto dto = new PlayerDto(42L, "Novak", "Djokovic", "N.DJO", "M",
                new CountryDto("SRB", "pic-srb"), "pic-p",
                new PlayerDataDto(1, 10000, 80000, 188, 36, List.of(1, 1, 0, 1, 1)));

        when(entityManager.getReference(Country.class, "SRB"))
                .thenReturn(new Country("SRB", "pic-srb"));
        // On renvoie l'entité passée à save pour simuler la persistance
        when(playerRepository.save(any(Player.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PlayerDto created = service.create(dto);

        // Assert
        assertThat(created).isNotNull();
        assertThat(created.id()).isEqualTo(42L);
        assertThat(created.firstname()).isEqualTo("Novak");
        assertThat(created.country()).isNotNull();
        assertThat(created.country().code()).isEqualTo("SRB");
        assertThat(created.data()).isNotNull();
        assertThat(created.data().rank()).isEqualTo(1);
        verify(entityManager).getReference(Country.class, "SRB");
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("exists: délègue au repository.existsById")
    void exists_delegates() {
        when(playerRepository.existsById(1L)).thenReturn(true);
        when(playerRepository.existsById(2L)).thenReturn(false);

        assertThat(service.exists(1L)).isTrue();
        assertThat(service.exists(2L)).isFalse();
        assertThat(service.exists(null)).isFalse();

        verify(playerRepository).existsById(1L);
        verify(playerRepository).existsById(2L);
        verifyNoMoreInteractions(playerRepository);
    }
}
