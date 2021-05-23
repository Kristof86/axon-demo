package be.sansoft.axondemo.accounts.web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author kristofennekens
 */
@Configuration
public class CustomWebMvcConfigurerer implements WebMvcConfigurer {
    private final List<String> allowedOrigins;

    CustomWebMvcConfigurerer(@Value("${security.webmvc.allowed-origins}") List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(this.allowedOrigins.toArray(new String[]{}))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
    }
}
