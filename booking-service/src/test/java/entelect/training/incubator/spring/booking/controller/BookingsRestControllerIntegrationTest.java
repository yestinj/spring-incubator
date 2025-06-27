package entelect.training.incubator.spring.booking.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.booking.BookingServiceApplication;
import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingRequest;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import entelect.training.incubator.spring.booking.service.CustomerClientService;
import entelect.training.incubator.spring.booking.service.FlightClientService;
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
import java.util.List;

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

        when(customerClientService.isValidCustomer(1)).thenReturn(true);
        when(flightClientService.isValidFlight(1)).thenReturn(true);

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

        when(customerClientService.isValidCustomer(1)).thenReturn(true);

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

}