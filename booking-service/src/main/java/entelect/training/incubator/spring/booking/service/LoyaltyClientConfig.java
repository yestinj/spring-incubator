package entelect.training.incubator.spring.booking.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class LoyaltyClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("entelect.training.incubator.spring.booking.gen");
        return marshaller;
    }
    @Bean
    public LoyaltyClientService countryClient(Jaxb2Marshaller marshaller) {
        LoyaltyClientService client = new LoyaltyClientService();
        client.setDefaultUri("http://localhost:8208/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
