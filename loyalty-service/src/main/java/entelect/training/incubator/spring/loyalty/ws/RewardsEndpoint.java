package entelect.training.incubator.spring.loyalty.ws;

import entelect.training.incubator.spring.loyalty.server.RewardsService;
import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsRequest;
import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsResponse;
import entelect.training.incubator.spring.loyalty.ws.model.RewardsBalanceRequest;
import entelect.training.incubator.spring.loyalty.ws.model.RewardsBalanceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigDecimal;

@Endpoint
@Tag(name = "rewards", description = "Rewards endpoint")
public class RewardsEndpoint {
    
    private static final String NAMESPACE_URI = "http://entelect.training/incubator/spring-loyalty-service";
    
    private final RewardsService rewardsService;
    
    public RewardsEndpoint(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @Tag(name = "captureRewards", description = "Capture the given rewards")
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "captureRewardsRequest")
    @ResponsePayload
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    public CaptureRewardsResponse captureRewards(@RequestPayload CaptureRewardsRequest request) {
        final BigDecimal balance = this.rewardsService.updateBalance(request.getPassportNumber(), request.getAmount());
    
        final CaptureRewardsResponse response = new CaptureRewardsResponse();
        response.setBalance(balance);
        return response;
    }

    @Tag(name = "rewardsBalance", description = "Retrieve the rewards balance for a customer")
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "rewardsBalanceRequest")
    @ResponsePayload
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    public RewardsBalanceResponse rewardsBalance(@RequestPayload RewardsBalanceRequest request) {
        final BigDecimal balance = this.rewardsService.getBalance(request.getPassportNumber());
        
        final RewardsBalanceResponse response = new RewardsBalanceResponse();
        response.setBalance(balance);
        return response;
    }
}
