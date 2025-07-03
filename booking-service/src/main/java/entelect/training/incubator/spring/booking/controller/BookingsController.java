package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.gen.CaptureRewardsResponse;
import entelect.training.incubator.spring.booking.jms.TextMessage;
import entelect.training.incubator.spring.booking.model.*;
import entelect.training.incubator.spring.booking.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final CustomerClientService customerClientService;
    private final FlightClientService flightClientService;
    private final LoyaltyClientService loyaltyClientService;
    private final NotificationService notificationService;
    private final BookingsService bookingService;

    public BookingsController(CustomerClientService customerClientService,
                              FlightClientService flightClientService,
                              LoyaltyClientService loyaltyClientService,
                              NotificationService notificationService,
                              BookingsService bookingsService) {
        this.customerClientService = customerClientService;
        this.flightClientService = flightClientService;
        this.loyaltyClientService = loyaltyClientService;
        this.notificationService = notificationService;
        this.bookingService = bookingsService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        LOGGER.info("Processing booking creation request for booking={}", bookingRequest);

        // validate
        Optional<Customer> customer = customerClientService.fetchCustomer(bookingRequest.getCustomerId());
        if (customer.isEmpty()) {
            LOGGER.warn("Customer not found for booking={}", bookingRequest.getCustomerId());
            return ResponseEntity.badRequest().body("Customer does not exist"); // Or throw a custom exception
        }

        Optional<Flight> flight = flightClientService.fetchFlightDetails(bookingRequest.getFlightId());
        if (flight.isEmpty()) {
            LOGGER.warn("Flight not found for booking={}", bookingRequest.getFlightId());
            return ResponseEntity.badRequest().body("Flight does not exist"); // Or throw a custom exception
        }

        final Booking savedBooking = bookingService.createBooking(bookingRequest);

        CaptureRewardsResponse rewardsResponse = loyaltyClientService.captureRewards(customer.get().getPassportNumber(), flight.get().getSeatCost());
        LOGGER.info("Loyalty capture rewards balance {}", rewardsResponse.getBalance());

        TextMessage textMessage = new TextMessage(
                customer.get().getPhoneNumber(),
                "Molo Air: Confirming flight %s booked for %s %s on %s."
                        .formatted(
                                flight.get().getFlightNumber(),
                                customer.get().getFirstName(),
                                customer.get().getLastName(),
                                flight.get().getDepartureTime()
                        )
        );
        notificationService.sendMessage(textMessage);
        LOGGER.info("Adding notification to queue {}", textMessage);

        LOGGER.trace("Booking created");
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {
        LOGGER.info("Processing booking search request for booking id={}", id);
        Booking booking = bookingService.getBooking(id);

        if (booking != null) {
            LOGGER.trace("Found booking");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }

        LOGGER.trace("Booking not found");
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchBookings(@RequestBody BookingSearchRequest searchRequest) {
        LOGGER.info("Processing booking search request for request {}", searchRequest);

        Booking booking = bookingService.searchBookings(searchRequest);

        if (booking != null) {
            return ResponseEntity.ok(booking);
        }

        LOGGER.trace("Booking not found");
        return ResponseEntity.notFound().build();
    }

}
