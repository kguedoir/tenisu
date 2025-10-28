package fr.latelier.tenisu.apis.domain;

import static fr.latelier.tenisu.apis.common.NamingConstants.*;
import static java.util.Objects.requireNonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entité JPA représentant un pays (code ISO3 et image/drapeau).
 */
@Entity
@Table(name = COUNTRIES)
@Getter
@NoArgsConstructor
public class Country {

    @Id
    @Column(name = CODE, length = LEN_CODE, nullable = false, updatable = false)
    private String code;

    @Column(name = PICTURE, length = LEN_PICTURE)
    private String picture;

    public Country(String code, String picture) {
        this.code = requireNonNull(code, "code must not be null");
        this.picture = requireNonNull(picture, "picture must not be null");
    }
}
