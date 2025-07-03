package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.jms.TextMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JmsTemplate jmsTemplate;

    public NotificationService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(TextMessage message) {
        jmsTemplate.convertAndSend("my.queue", message);
    }
}
