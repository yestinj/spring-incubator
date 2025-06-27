package entelect.training.incubator.spring.booking.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.booking.BookingServiceApplication;
import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingRequest;
import entelect.training.incubator.spring.booking.model.Customer;
import entelect.training.incubator.spring.booking.model.Flight;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import entelect.training.incubator.spring.booking.service.CustomerClientService;
import entelect.training.incubator.spring.booking.service.FlightClientService;
import entelect.training.incubator.spring.booking.service.LoyaltyClientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookingServiceApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingsRestControllerIntegrationTest {

    @MockBean
    private CustomerClientService customerClientService;

    @MockBean
    private FlightClientService flightClientService;

    @MockBean
    private LoyaltyClientService loyaltyClientService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookingRepository bookingRepository;

    @AfterEach
    void resetDb() {
        bookingRepository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1);
        bookingRequest.setFlightId(1);

        Customer mockCustomer = generateMockCustomer();
        when(customerClientService.fetchCustomer(1)).thenReturn(Optional.of(mockCustomer));

        Flight mockFlight = generateMockFlight();
        when(flightClientService.fetchFlightDetails(1)).thenReturn(Optional.of(mockFlight));

        mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content(toJson(bookingRequest)))
                .andExpectAll(status().isCreated());

        List<Booking> found = (List<Booking>) bookingRepository.findAll();
        assertThat(found).extracting(Booking::getCustomerId).containsOnly(1);
    }

    @Test
    void whenCustomerDoesNotExist_thenDontCreateBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1);
        bookingRequest.setFlightId(1);

        mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content(toJson(bookingRequest)))
                .andExpectAll(
                        status().is4xxClientError(),
                        content().string("Customer does not exist")
                );

        List<Booking> found = (List<Booking>) bookingRepository.findAll();
        assertThat(found).extracting(Booking::getCustomerId).isEmpty();
    }

    @Test
    void whenFlightDoesNotExist_thenDontCreateBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1);
        bookingRequest.setFlightId(1);

        Customer mockCustomer = generateMockCustomer();
        when(customerClientService.fetchCustomer(1)).thenReturn(Optional.of(mockCustomer));

        mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content(toJson(bookingRequest)))
                .andExpectAll(
                        status().is4xxClientError(),
                        content().string("Flight does not exist")
                );

        List<Booking> found = (List<Booking>) bookingRepository.findAll();
        assertThat(found).extracting(Booking::getCustomerId).isEmpty();
    }

    private static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    private static Flight generateMockFlight() {
        Flight mockFlight = new Flight();
        mockFlight.setId(1);
        mockFlight.setFlightNumber("OS123");
        mockFlight.setOrigin("London");
        mockFlight.setDestination("New York");
        mockFlight.setArrivalTime(LocalDateTime.of(2025,6,1,12,0));
        mockFlight.setDepartureTime(LocalDateTime.of(2025,6,1,10,0));
        mockFlight.setSeatCost(999.99f);
        mockFlight.setSeatsAvailable(99);
        return mockFlight;
    }

    private static Customer generateMockCustomer() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1);
        mockCustomer.setEmail("<EMAIL>");
        mockCustomer.setFirstName("John");
        mockCustomer.setLastName("Doe");
        mockCustomer.setPhoneNumber("123456789");
        mockCustomer.setPassportNumber("123456789");
        mockCustomer.setUsername("johndoe");
        return mockCustomer;
    }

}