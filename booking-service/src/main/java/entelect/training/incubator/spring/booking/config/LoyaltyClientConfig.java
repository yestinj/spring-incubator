package entelect.training.incubator.spring.booking.config;

import entelect.training.incubator.spring.booking.gen.CaptureRewardsRequest;
import entelect.training.incubator.spring.booking.gen.CaptureRewardsResponse;
import entelect.training.incubator.spring.booking.service.LoyaltyClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class LoyaltyClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
                CaptureRewardsRequest.class,
                CaptureRewardsResponse.class
        );
        return marshaller;
    }

    @Bean
    public LoyaltyClientService loyaltyClientService(Jaxb2Marshaller marshaller) {
        LoyaltyClientService client = new LoyaltyClientService();
        client.setDefaultUri("http://localhost:8208/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
