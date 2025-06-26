package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingRequest;
import entelect.training.incubator.spring.booking.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.service.BookingsService;
import entelect.training.incubator.spring.booking.service.CustomerClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final CustomerClientService customerClientService;
    private final BookingsService bookingService;

    public BookingsController(CustomerClientService customerClientService, BookingsService bookingsService) {
        this.customerClientService = customerClientService;
        this.bookingService = bookingsService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        LOGGER.info("Processing booking creation request for booking={}", bookingRequest);

        // validate
        boolean isValidCustomer = customerClientService.isValidCustomer(bookingRequest.getCustomerId());
        if (!isValidCustomer) {
            LOGGER.warn("Customer not found for booking={}", bookingRequest.getCustomerId());
            return ResponseEntity.badRequest().body(null); // Or throw a custom exception
        }

        final Booking savedCustomer = bookingService.createBooking(bookingRequest);

        LOGGER.trace("Booking created");
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
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