package entelect.training.incubator.spring.customer.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerSearchRequest {
    @NotNull
    private SearchType searchType;

    private String firstName;
    private String lastName;
    private String passport;
    private String username;
}
