package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.gen.CaptureRewardsRequest;
import entelect.training.incubator.spring.booking.gen.CaptureRewardsResponse;
import jakarta.xml.soap.MimeHeaders;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import java.math.BigDecimal;

public class LoyaltyClientService extends WebServiceGatewaySupport {

    public CaptureRewardsResponse captureRewards(String passportNumber, double amount) {
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setPassportNumber(passportNumber);
        request.setAmount(BigDecimal.valueOf(amount));

        return (CaptureRewardsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        request,
                        message -> {
                            // Add SOAP action header
                            ((SoapMessage)message).setSoapAction(
                                    "http://entelect.training/incubator/spring-loyalty-service/captureRewards");
                            SaajSoapMessage soapMessage = (SaajSoapMessage) message;
                            MimeHeaders mimeHeader = soapMessage.getSaajMessage().getMimeHeaders();
                            // todo should this use the incoming auth header instead?
                            mimeHeader.setHeader("Authorization", "Basic dXNlcjp0aGVfY2FrZQ==");
                        }
                );
    }

}
