package tourGuide.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tripPricer.TripPricer;

@Configuration
public class ContextConfiguration {

    @Bean
    public TripPricer tripPricer() {
        return new TripPricer();
    }
}
