package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class FlightClientService {

    private final RestTemplate restTemplate;
    private final String flightServiceUrl;
    private static final Logger LOGGER = LoggerFactory.getLogger(FlightClientService.class);

    public FlightClientService(RestTemplate restTemplate, @Value("${flights.service.url}") String flightServiceUrl) {
        this.restTemplate = restTemplate;
        this.flightServiceUrl = flightServiceUrl;
    }

    public boolean isValidFlight(Integer flightId) {
        String url = flightServiceUrl + "/" + flightId;
        LOGGER.info("Calling flight service URL: {}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("user:the_cake".getBytes()));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Flight> response = restTemplate.exchange(url, HttpMethod.GET, entity, Flight.class);
            LOGGER.info("Flight service response for flightId={}: status={}, body={}",
                    flightId, response.getStatusCode(), response.getBody());
            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null;
        } catch (HttpClientErrorException ex) {
            LOGGER.error("Flight service error for flightId={}: status={}, message={}",
                    flightId, ex.getStatusCode(), ex.getMessage());
            return false;
        } catch (Exception ex) {
            LOGGER.error("Unexpected error for customerId={}: message={}", flightId, ex.getMessage());
            return false;
        }
    }
}