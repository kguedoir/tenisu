package fr.latelier.tenisu.apis.web;

/**
 * Constantes Web génériques.
 * Remarque Sonar: préférer une classe utilitaire finale plutôt qu'une interface de constantes.
 */
public final class WebConstants {

    private WebConstants() { /* no-op */ }

    /**
     * Constantes pour les chemins (path) communs utilisés dans les APIS de l’application.
     * <p>
     * Ces chemins ne tiennent pas compte du {@code context path} <a href="https://www.baeldung.com/spring-boot-context-path">configuré pour
     * l'application Spring Boot</a>.
     */
    public static final class Path {
        private Path() { /* no-op */ }
        /**
         * La racine de tous les chemins de l’application.
         */
        public static final String ROOT = "/";

        /**
         * La racine de tous les chemins aux APIs de l’application.
         */
        public static final String APIS = ROOT + "apis";

        // Base des endpoints des joueurs (conserve le comportement actuel)
        public static final String PLAYERS_BASE = WebConstants.Path.APIS + "/players";

        // Fragments de chemins réutilisables
        public static final String PATH_ID = "/{id}";
        public static final String HEALTH = "/health";
        public static final String STATS = "/stats";

    }
}
