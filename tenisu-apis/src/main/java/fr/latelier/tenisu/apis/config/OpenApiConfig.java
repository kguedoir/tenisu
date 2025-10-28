package fr.latelier.tenisu.apis.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Tenisu",
                version = "v1",
                description = "APIs REST pour les joueurs et les statistiques de Tenisu",
                contact = @Contact(name = "L'Atelier", url = "https://latelier.fr"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        )
)
public class OpenApiConfig {
}
