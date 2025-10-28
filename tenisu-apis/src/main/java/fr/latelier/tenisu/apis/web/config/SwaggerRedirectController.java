package fr.latelier.tenisu.apis.web.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Convenience redirects for common Swagger UI entry URLs.
 *
 * springdoc-openapi (v2.x) serves the UI at /swagger-ui/index.html by default.
 * Many users try /swagger-ui.html or /swagger; these mappings redirect them to the right place.
 */
@Controller
public class SwaggerRedirectController {

    @GetMapping({"/swagger-ui.html", "/swagger", "/api-docs", "/docs"})
    @ResponseStatus(HttpStatus.FOUND)
    public String redirectToSwaggerUi() {
        return "redirect:/swagger-ui/index.html";
    }
}
