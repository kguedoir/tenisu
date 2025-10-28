package fr.latelier.tenisu.apis.web.error;

import lombok.Getter;

/**
 * Exception fonctionnelle indiquant qu'une ressource demandée n'a pas été trouvée.
 */
@Getter
public class NotFoundException extends RuntimeException {
    private final String resource;
    private final String key;

    public NotFoundException(String resource, String key) {
        super(resource + " not found: " + key);
        this.resource = resource;
        this.key = key;
    }

}
