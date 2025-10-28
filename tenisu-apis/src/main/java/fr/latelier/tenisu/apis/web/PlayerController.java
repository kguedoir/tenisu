package fr.latelier.tenisu.apis.web;

import fr.latelier.tenisu.apis.common.NamingConstants;
import fr.latelier.tenisu.apis.service.PlayerService;
import fr.latelier.tenisu.apis.web.dto.PlayerDto;
import fr.latelier.tenisu.apis.web.dto.StatsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST exposant les opérations liées aux joueurs.
 */
@RestController
@RequestMapping(PlayerController.BASE)
@CrossOrigin(origins = "http://localhost:4200") // Autoriser les requêtes CORS depuis Angular en dev
@Tag(name = "Joueurs", description = "Opérations liées aux joueurs de tennis")
public class PlayerController {

    public static final String BASE = WebConstants.Path.PLAYERS_BASE;


    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Recherche des joueurs par nom/prénom/surnom.
     *
     * @param search terme de recherche optionnel
     * @return liste des joueurs correspondants
     */
    @Operation(summary = "Rechercher des joueurs par nom", description = "Recherche par prénom, nom ou surnom (insensible à la casse)")
    @ApiResponse(responseCode = "200", description = "Liste des joueurs correspondants",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDto.class)))
    @GetMapping()
    public List<PlayerDto> searchPlayers(@Parameter(description = "Nom à rechercher (prénom, nom ou surnom)") @RequestParam(value = NamingConstants.SEARCH, required = false) String search) {
        return playerService.searchByName(search);
    }

    /**
     * Récupère un joueur par son identifiant.
     *
     * @param id identifiant du joueur
     * @return 200 avec le joueur, ou 404 si non trouvé
     */
    @Operation(summary = "Récupérer un joueur par ID")
    @ApiResponse(responseCode = "200", description = "Joueur trouvé",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDto.class)))
    @ApiResponse(responseCode = "404", description = "Joueur non trouvé", content = @Content)
    @GetMapping(WebConstants.Path.PATH_ID)
    public ResponseEntity<PlayerDto> getPlayerById(@Parameter(description = "Identifiant du joueur") @PathVariable(NamingConstants.ID) Long id) {
        PlayerDto player = playerService.getById(id);
        return ResponseEntity.ok(player);
    }

    /**
     * Renvoie les statistiques agrégées.
     *
     * @return les statistiques calculées (pays gagnant, IMC moyen, taille médiane)
     */
    @Operation(summary = "Récupérer les statistiques agrégées des joueurs")
    @ApiResponse(responseCode = "200", description = "Statistiques",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatsDto.class)))
    @GetMapping("/stats")
    public StatsDto getStats() {
        return playerService.getStats();
    }

    /**
     * Crée un nouveau joueur.
     *
     * @param player le joueur à créer (JSON)
     * @return 201 Created avec l'entité créée et l'en-tête Location
     */
    @Operation(summary = "Créer un nouveau joueur")
    @ApiResponse(responseCode = "201", description = "Joueur créé",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDto.class)))
    @PostMapping
    public ResponseEntity<PlayerDto> create(@RequestBody PlayerDto player) {
        PlayerDto created = playerService.create(player);
        return ResponseEntity.created(java.net.URI.create(BASE + "/" + created.id())).body(created);
    }
}
