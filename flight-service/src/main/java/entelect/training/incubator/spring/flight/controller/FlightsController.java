package entelect.training.incubator.spring.flight.controller;

import entelect.training.incubator.spring.flight.model.Flight;
import entelect.training.incubator.spring.flight.model.FlightsSearchRequest;
import entelect.training.incubator.spring.flight.service.FlightsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("flights")
@Tag(name = "flights", description = "Flights controller")
public class FlightsController {

    private final Logger LOGGER = LoggerFactory.getLogger(FlightsController.class);

    private final FlightsService flightsService;

    public FlightsController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @Tag(name = "createFlight", description = "Create a flight")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    public ResponseEntity<?> createFlight(@RequestBody Flight flight) {
        LOGGER.info("Processing flight creation request for flight={}", flight);

        final Flight savedFlight = flightsService.createFlight(flight);

        LOGGER.trace("Flight created");
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    @Tag(name = "getFlights", description = "Fetch all flights")
    @GetMapping()
    public ResponseEntity<?> getFlights() {
        LOGGER.info("Fetching all flights");
        List<Flight> flights = this.flightsService.getFlights();

        if (!flights.isEmpty()) {
            LOGGER.trace("Found flights");
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }

        LOGGER.trace("No flights found");
        return ResponseEntity.notFound().build();
    }

    @Tag(name = "getFlight", description = "Fetch a flight by id")
    @GetMapping("{id}")
    public ResponseEntity<?> getFlightById(@PathVariable Integer id) {
        LOGGER.info("Processing flight search request for flight id={}", id);
        Flight flight = this.flightsService.getFlight(id);

        if (flight != null) {
            LOGGER.trace("Found flight");
            return new ResponseEntity<>(flight, HttpStatus.OK);
        }

        LOGGER.trace("Flight not found");
        return ResponseEntity.notFound().build();
    }

    @Tag(name = "searchFlights", description = "Search for a flight")
    @PostMapping("/search")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    public ResponseEntity<?> searchFlights(@RequestBody FlightsSearchRequest searchRequest) {
        LOGGER.info("Processing flight search request: {}", searchRequest);

        List<Flight> flights = flightsService.searchFlights(searchRequest);

        if (!flights.isEmpty()) {
            LOGGER.trace("Found flights: {}", flights);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }

        LOGGER.trace("No flights found");
        return ResponseEntity.notFound().build();
    }

    @Tag(name = "getFlightSpecials", description = "Fetch flight specials")
    @GetMapping("/specials")
    public List<Flight> getFlightSpecials() {
        LOGGER.info("Processing flight specials request");

        List<Flight> discountedFlights = flightsService.getDiscountedFlights();

        LOGGER.trace("Flight specials: {}", discountedFlights);
        return discountedFlights;
    }
}
