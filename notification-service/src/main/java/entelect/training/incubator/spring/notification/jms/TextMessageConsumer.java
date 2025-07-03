package entelect.training.incubator.spring.notification.jms;

import entelect.training.incubator.spring.notification.sms.client.impl.MoloCellSmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class TextMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TextMessageConsumer.class);

    private final MoloCellSmsClient moloCellSmsClient;

    public TextMessageConsumer(MoloCellSmsClient moloCellSmsClient) {
        this.moloCellSmsClient = moloCellSmsClient;
    }

    @JmsListener(destination = "my.queue")
    public void receiveMessage(TextMessage message) {
        logger.info("Received message: phoneNumber={}, message={}",
                message.getPhoneNumber(), message.getMessage());
        moloCellSmsClient.sendSms(message.getPhoneNumber(), message.getMessage());
    }
}