package fr.latelier.tenisu.apis.service.impl;

import static org.apache.logging.log4j.util.Strings.EMPTY;

import fr.latelier.tenisu.apis.domain.Country;
import fr.latelier.tenisu.apis.domain.Player;
import fr.latelier.tenisu.apis.domain.PlayerData;
import fr.latelier.tenisu.apis.repository.PlayerRepository;
import fr.latelier.tenisu.apis.service.PlayerService;
import fr.latelier.tenisu.apis.web.dto.CountryDto;
import fr.latelier.tenisu.apis.web.dto.CountryWinRatioDto;
import fr.latelier.tenisu.apis.web.dto.PlayerDataDto;
import fr.latelier.tenisu.apis.web.dto.PlayerDto;
import fr.latelier.tenisu.apis.web.dto.StatsDto;
import fr.latelier.tenisu.apis.web.error.NotFoundException;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service applicatif pour la gestion des joueurs.
 * Fournit la recherche, la récupération par identifiant et le calcul de statistiques.
 */
@Service
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

    private static final String SQL_REGEX = "(['\";\\\\]|--|\\b(OR|AND|SELECT|INSERT|DELETE|UPDATE|DROP|EXEC|UNION|CREATE|ALTER|TRUNCATE|GRANT|REVOKE)\\b)";
    private static final String CMD_REGEX = "[^\\w\\s\\-\\.]";
    private final PlayerRepository playerRepository;
    private final jakarta.persistence.EntityManager entityManager;

    @Override
    public boolean exists(Long id) {
        return id != null && playerRepository.existsById(id);
    }

    public PlayerServiceImpl(PlayerRepository playerRepository, jakarta.persistence.EntityManager entityManager) {
        this.playerRepository = playerRepository;
        this.entityManager = entityManager;
    }

    /**
     * Recherche des joueurs par nom (prénom, nom ou surnom), insensible à la casse.
     * @param name terme de recherche saisi par l'utilisateur
     * @return la liste des joueurs correspondants, triée par rang croissant
     */
    @Override
    public List<PlayerDto> searchByName(String name) {
        Optional<String> q = normalizedQuery(name);
        List<Player> players = q.isPresent()
                ? playerRepository
                        .findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrShortnameContainingIgnoreCaseOrderByDataRankAsc(q.get(), q.get(), q.get())
                : playerRepository.findAllByOrderByDataRankAsc();
        return players.stream().map(PlayerDto::fromEntity).toList();
    }

    /**
     * Normalise la requête utilisateur et applique un nettoyage afin d'éviter
     * les injections SQL et de commandes. La sélection du dépôt est gérée ailleurs.
     */
    private static Optional<String> normalizedQuery(String name) {
        if (name == null) return Optional.empty();
        String q = name.trim();
        if (q.isEmpty()) return Optional.empty();

        // Nettoyage de l'entrée pour prévenir l'injection SQL et l'injection de commandes
        // Supprimer les caractères et motifs dangereux
        q = q.replaceAll(SQL_REGEX, EMPTY);
        // Limiter éventuellement aux caractères alphanumériques et ponctuation de base
        q = q.replaceAll(CMD_REGEX, EMPTY);

        return q.isEmpty() ? Optional.empty() : Optional.of(q);
    }

    /**
     * Crée un nouveau joueur à partir d'un DTO et le persiste.
     * Si le pays fourni n'existe pas encore, il est ajouté en base avant la création du joueur.
     * @param dto payload représentant le joueur
     * @return le joueur créé, remappé en DTO
     */
    @Override
    @Transactional // readOnly = false implicite
    public PlayerDto create(PlayerDto dto) {
        if (dto == null) return null;
        CountryDto c = dto.country();
        Country countryRef = null;
        if (c != null && c.code() != null) {
            // Recherche du pays existant
            countryRef = entityManager.find(Country.class, c.code());
            if (countryRef == null) {
                // Création si absent; picture peut être vide mais non null (constructeur impose non null)
                String picture = (c.picture() == null) ? "" : c.picture();
                countryRef = new Country(c.code(), picture);
                entityManager.persist(countryRef);
            }
        }

        PlayerDataDto dd = dto.data();
        PlayerData data = (dd == null) ? null : new PlayerData(dd.rank(), dd.points(), dd.weight(), dd.height(), dd.age());
        List<Integer> last = (dd == null) ? null : dd.lastResults();

        Player entity = new Player(dto.firstname(), dto.lastname(), dto.shortname(), dto.sex(), countryRef, dto.picture(), data, last);
        Player saved = playerRepository.save(entity);
        return PlayerDto.fromEntity(saved);
    }

    /**
     * Récupère un joueur par identifiant.
     * @param id identifiant du joueur (peut être null)
     * @return le joueur sous forme de DTO
     * @throws NotFoundException si non trouvé
     */
    @Override
    public PlayerDto getById(Long id) {
        if (id == null) {
            throw new NotFoundException("Player", "null");
        }
        return playerRepository.findById(id)
                .map(PlayerDto::fromEntity)
                .orElseThrow(() -> new NotFoundException("Player", String.valueOf(id)));
    }

    /**
     * Calcule les statistiques agrégées sur l'ensemble des joueurs.
     * @return DTO des statistiques
     */
    @Override
    public StatsDto getStats() {
        List<Player> players = playerRepository.findAllWithDetails();

        // Calcul du meilleur pays par ratio de victoires
        CountryWinRatioDto bestCountry = computeBestCountryWinRatio(players);

        // IMC moyen
        double averageBmi = computeAverageBmi(players);

        // Taille médiane en cm
        double medianHeight = computeMedianHeight(players);

        return new StatsDto(bestCountry, round(averageBmi, 2), round(medianHeight, 1));
    }

    private static final class Acc {
        long wins = 0;
        long total = 0;
        String picture = null;

        void addFromLast(List<Integer> lastResults) {
            if (lastResults == null || lastResults.isEmpty()) return;
            // ne compter que les entrées non nulles
            long valid = lastResults.stream().filter(Objects::nonNull).count();
            long w = lastResults.stream().filter(Objects::nonNull).filter(i -> i == 1).count();
            this.total += valid;
            this.wins += w;
        }
    }

    private static boolean isEligibleForCountryStats(Player p) {
        return p != null
                && p.getCountry() != null
                && p.getCountry().getCode() != null
                && p.getLast() != null;
    }

    private CountryWinRatioDto computeBestCountryWinRatio(List<Player> players) {
        if (players == null || players.isEmpty()) return null;

        Map<String, Acc> byCountry = new HashMap<>();
        for (Player p : players) {
            if (!isEligibleForCountryStats(p)) continue;
            String code = p.getCountry().getCode();
            Acc acc = byCountry.computeIfAbsent(code, k -> new Acc());
            if (acc.picture == null) acc.picture = p.getCountry().getPicture();
            acc.addFromLast(p.getLast());
        }

        return byCountry.entrySet().stream()
                .filter(e -> e.getValue().total > 0)
                .map(e -> new CountryWinRatioDto(
                        e.getKey(),
                        e.getValue().picture,
                        (double) e.getValue().wins / (double) e.getValue().total
                ))
                .max(Comparator.comparingDouble(CountryWinRatioDto::winRatio))
                .orElse(null);
    }

    // SRP : isoler la validation et le calcul de l'IMC
    private static boolean hasValidMeasures(PlayerData data) {
        return data != null
                && data.getWeight() != null
                && data.getHeight() != null
                && data.getHeight() != 0;
    }

    private static OptionalDouble safeBmi(PlayerData data) {
        if (!hasValidMeasures(data)) return OptionalDouble.empty();
        double bmi = bmiFromGramsAndCm(data.getWeight(), data.getHeight());
        return Double.isFinite(bmi) ? OptionalDouble.of(bmi) : OptionalDouble.empty();
    }

    private double computeAverageBmi(List<Player> players) {
        if (players == null || players.isEmpty()) return 0.0;
        double sum = 0.0;
        int count = 0;
        for (Player p : players) {
            PlayerData d = p != null ? p.getData() : null;
            OptionalDouble bmi = safeBmi(d);
            if (bmi.isPresent()) {
                sum += bmi.getAsDouble();
                count++;
            }
        }
        return count == 0 ? 0.0 : (sum / count);
    }

    private double computeMedianHeight(List<Player> players) {
        if (players == null || players.isEmpty()) return 0.0;
        List<Integer> heights = players.stream()
                .map(PlayerServiceImpl::extractHeight)
                .filter(PlayerServiceImpl::isPositive)
                .sorted()
                .toList();
        int n = heights.size();
        if (n == 0) return 0.0;
        if (n % 2 == 1) {
            return heights.get(n / 2);
        } else {
            return (heights.get(n / 2 - 1) + heights.get(n / 2)) / 2.0;
        }
    }

    // SRP : encapsuler l'extraction/validation de la taille
    private static Integer extractHeight(Player p) {
        return (p != null && p.getData() != null) ? p.getData().getHeight() : null;
    }

    private static boolean isPositive(Integer h) {
        return h != null && h > 0;
    }

    private static double bmiFromGramsAndCm(int weightGrams, int heightCm) {
        // IMC = kg / (m^2) ; poids en kg = grammes/1000 ; taille en m = cm/100
        double kg = weightGrams / 1000.0;
        double m = heightCm / 100.0;
        if (m == 0.0) return 0.0;
        return kg / (m * m);
    }

    private static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
