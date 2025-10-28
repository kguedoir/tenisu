package fr.latelier.tenisu.apis.config;

import java.io.IOException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class SpaWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward the root path to index.html so the SPA loads at /
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static files produced by Angular from classpath:/public/ (already copied by Maven during build)
        // and fallback to index.html for non-file, non-API routes (SPA deep links).
        registry
            .addResourceHandler("/**")
            .addResourceLocations("classpath:/public/browser/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Resource getResource(String resourcePath, Resource location) throws IOException {
                    // Do not SPA-forward backend or tooling endpoints; let controllers handle them or return 404
                    if (resourcePath.startsWith("apis/")
                            || resourcePath.startsWith("h2-console/")
                            || resourcePath.startsWith("actuator/")
                            || resourcePath.startsWith("v3/api-docs")
                            || resourcePath.startsWith("swagger-ui")) {
                        return null; // fall through to other handlers
                    }

                    // If the requested static asset exists, serve it
                    Resource requested = location.createRelative(resourcePath);
                    if (requested.exists() && requested.isReadable()) {
                        return requested;
                    }

                    // If it's not a direct static asset (likely a client route), serve index.html for SPA
                    return location.createRelative("index.html");
                }
            });
    }
}
