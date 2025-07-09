package entelect.training.incubator.spring.booking.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingSearchRequest {

    @NotNull
    private SearchType searchType;

    private Integer customerId;
    private String referenceNumber;
}
