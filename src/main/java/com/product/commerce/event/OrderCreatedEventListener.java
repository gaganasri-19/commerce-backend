package com.product.commerce.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class OrderCreatedEventListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderCreatedEventListener(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedEvent event) {

        kafkaTemplate.send(
                "order.created",
                event.getOrderId().toString(), //key
                event
        );
    }
}
