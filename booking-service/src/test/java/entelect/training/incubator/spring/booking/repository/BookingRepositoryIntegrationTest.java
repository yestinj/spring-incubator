package entelect.training.incubator.spring.booking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class BookingRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

}