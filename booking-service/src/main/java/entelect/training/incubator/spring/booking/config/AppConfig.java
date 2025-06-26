package entelect.training.incubator.spring.booking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        LOGGER.info("Creating RestTemplate bean");
        return new RestTemplate();
    }
}
