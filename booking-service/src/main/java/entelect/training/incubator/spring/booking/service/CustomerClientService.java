package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Optional;

@Service
public class CustomerClientService {

    private final RestTemplate restTemplate;
    private final String customerServiceUrl;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerClientService.class);

    public CustomerClientService(RestTemplate restTemplate, @Value("${customers.service.url}") String customerServiceUrl) {
        this.restTemplate = restTemplate;
        this.customerServiceUrl = customerServiceUrl;
    }

    public Optional<Customer> fetchCustomer(Integer customerId) {
        String url = customerServiceUrl + "/" + customerId;
        LOGGER.info("Calling customer service URL: {}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("user:the_cake".getBytes()));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Customer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Customer.class);
            LOGGER.info("Customer service response for customerId={}: status={}, body={}",
                    customerId, response.getStatusCode(), response.getBody());
            return response.getStatusCode() == HttpStatus.OK ? Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (HttpClientErrorException ex) {
            LOGGER.error("Customer service error for customerId={}: status={}, message={}",
                    customerId, ex.getStatusCode(), ex.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            LOGGER.error("Unexpected error for customerId={}: message={}", customerId, ex.getMessage());
            return Optional.empty();
        }
    }
}