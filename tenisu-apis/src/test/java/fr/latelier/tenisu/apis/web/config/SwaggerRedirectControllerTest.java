package fr.latelier.tenisu.apis.web.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(SwaggerRedirectController.class)
class SwaggerRedirectControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("/swagger-ui.html redirects to /swagger-ui/index.html")
    void swaggerUiHtml_redirects() throws Exception {
        mvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/swagger-ui/index.html"));
    }

    @Test
    @DisplayName("/swagger redirects to /swagger-ui/index.html")
    void swagger_redirects() throws Exception {
        mvc.perform(get("/swagger"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/swagger-ui/index.html"));
    }
}
