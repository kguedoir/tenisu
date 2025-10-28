package fr.latelier.tenisu.apis.common;

/**
 * Naming constants
 *
 * Classe unique, générale et globale regroupant toutes les constantes de nommage
 * utilisées dans les annotations (Web/Spring, DTO/Jackson, JPA) et ailleurs.
 *
 * Règles Sonar respectées: classe utilitaire finale avec constructeur privé,
 * sous-espaces de noms via classes imbriquées statiques.
 */
public final class NamingConstants {

    private NamingConstants() { /* no-op */ }

    public static final String COUNTRIES = "countries";
    public static final String CODE = "code";
    public static final int LEN_CODE = 3;
    public static final String PICTURE = "picture";
    public static final int LEN_PICTURE = 512;
    public static final String SEARCH = "search";
    public static final String RANK = "rank";
    public static final String POINTS = "points";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String AGE = "age";
    public static final String LAST = "last";
    public static final String COUNTRY = "country";

    public static final String PLAYERS = "players";

    public static final String ID = "id";
    public static final String FIRSTNAME = "firstname";
    public static final int LEN_FIRSTNAME = 100;
    public static final String LASTNAME = "lastname";
    public static final int LEN_LASTNAME = 100;
    public static final String SHORTNAME = "shortname";
    public static final int LEN_SHORTNAME = 20;
    public static final String SEX = "sex";
    public static final int LEN_SEX = 1;
    public static final String COUNTRY_CODE = "country_code";
    public static final String COUNTRY_REF_COL = CODE;
    public static final String TBL_LAST_RESULTS = "player_last_results";
    public static final String LAST_JOIN = "player_id";
    public static final String LAST_ORDER = "position";
    public static final String LAST_VALUE = "result_value";
    public static final String DATA = "data";
    public static final String WIN_RATIO = "winRatio";
    public static final String BEST_COUNTRY = "bestCountry";
    public static final String AVERAGE_BMI = "averageBmi";
    public static final String MEDIAN_HEIGHT_CM = "medianHeightCm";
}
