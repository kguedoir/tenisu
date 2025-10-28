package fr.latelier.tenisu.apis.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur de santé simple pour vérifier que le service est opérationnel.
 */
@RestController
@RequestMapping(WebConstants.Path.APIS)
public class HealthController {

    /**
     * Renvoie un petit payload indiquant l'état du service.
     * @return une map avec le statut et le nom du service
     */
    @GetMapping(WebConstants.Path.HEALTH)
    public Map<String, Object> health() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "UP");
        map.put("service", "tenisu-apis");
        return map;
    }
}
