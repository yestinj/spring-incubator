package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.config.LoyaltyClientConfig;
import entelect.training.incubator.spring.booking.gen.CaptureRewardsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = LoyaltyClientConfig.class)
public class LoyaltyClientServiceLiveTest {

    @Autowired
    LoyaltyClientService client;

    @Test
    public void givenLoyaltyService_canCaptureRewards() {
        CaptureRewardsResponse rewardsResponse = client.captureRewards("AAA", 999);
        assertEquals(BigDecimal.valueOf(999.0), rewardsResponse.getBalance());
    }
}
