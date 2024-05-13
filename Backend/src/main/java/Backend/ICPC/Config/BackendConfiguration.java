package Backend.ICPC.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class BackendConfiguration {
    //Supposedly this has a high chance of breaking if we use Spring Security.
    //There are some fixes for it to play nicely though, just for awareness.
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("*");
            }
        };
    }
}
