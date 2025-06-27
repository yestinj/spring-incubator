package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.gen.CaptureRewardsRequest;
import entelect.training.incubator.spring.booking.gen.CaptureRewardsResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;

public class LoyaltyClientService extends WebServiceGatewaySupport {

    public CaptureRewardsResponse captureRewards(String passportNumber, double amount) {
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setPassportNumber(passportNumber);
        request.setAmount(BigDecimal.valueOf(amount));

        return (CaptureRewardsResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }

}
