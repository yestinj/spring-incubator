package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingRequest;
import entelect.training.incubator.spring.booking.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.model.SearchType;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class BookingsService {

    private final BookingRepository bookingRepository;

    public BookingsService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(BookingRequest bookingRequest) {
        Booking booking = new Booking(bookingRequest.getCustomerId(), bookingRequest.getFlightId());
        // todo generate ref number
        booking.setReferenceNumber("ABCD1234");

        return bookingRepository.save(booking);
    }

    public Booking getBooking(Integer id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        return bookingOptional.orElse(null);
    }

    public Booking searchBookings(BookingSearchRequest searchRequest) {
        Map<SearchType, Supplier<Optional<Booking>>> searchStrategies = new HashMap<>();
        searchStrategies.put(SearchType.CUSTOMER_SEARCH, () -> bookingRepository.findByCustomerId(searchRequest.getCustomerId()));
        searchStrategies.put(SearchType.REFERENCE_SEARCH, () -> bookingRepository.findByReferenceNumber(searchRequest.getReferenceNumber()));

        Optional<Booking> customerOptional = searchStrategies.get(searchRequest.getSearchType()).get();

        return customerOptional.orElse(null);

    }
}
