package entelect.training.incubator.spring.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer customerId;

    @NotNull
    private Integer flightId;

    @NotBlank
    private String referenceNumber;

    public Booking(Integer customerId, Integer flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }
}

