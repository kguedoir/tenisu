package fr.latelier.tenisu.apis.domain;

import static fr.latelier.tenisu.apis.common.NamingConstants.*;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entité JPA représentant un joueur de tennis.
 */
@Entity
@Table(name = PLAYERS)
@Getter
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = FIRSTNAME, length = LEN_FIRSTNAME, nullable = false)
    private String firstname;

    @Column(name = LASTNAME, length = LEN_LASTNAME, nullable = false)
    private String lastname;

    @Column(name = SHORTNAME, length = LEN_SHORTNAME, nullable = false)
    private String shortname;

    @Column(name = SEX, length = LEN_SEX, nullable = false)
    private String sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COUNTRY_CODE, referencedColumnName = COUNTRY_REF_COL, nullable = false)
    private Country country;

    @Column(name = PICTURE, length = LEN_PICTURE)
    private String picture;

    @Embedded
    private PlayerData data;

    @ElementCollection
    @CollectionTable(name = TBL_LAST_RESULTS, joinColumns = @JoinColumn(name = LAST_JOIN))
    @OrderColumn(name = LAST_ORDER)
    @Column(name = LAST_VALUE)
    private List<Integer> last;

    /**
     * Constructeur complet pour créer un joueur.
     */
    public Player( String firstname, String lastname, String shortname, String sex, Country country, String picture, PlayerData data, List<Integer> last) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.shortname = shortname;
        this.sex = sex;
        this.country = country;
        this.picture = picture;
        this.data = data;
        this.last = (last == null) ? null : List.copyOf(last);
    }
}
