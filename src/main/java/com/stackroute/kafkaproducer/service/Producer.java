package com.stackroute.kafkaproducer.service;

import com.stackroute.kafkaproducer.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


/**
 * Producer class should be used to send messages to Kafka Topic
 * This class should use KafkaTemplate to send Customer message
 */
@Slf4j
@Service
public class Producer {

    @Value("${kafka.topic-name}")
    private String topic;

    /**
     * Inject a bean of KafkaTemplate created in KafkaConfig class
     */
    private KafkaTemplate<String, Customer> kafkaTemplate;

    @Autowired
    public Producer(KafkaTemplate<String, Customer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Create a method sendMessage(Customer message)
     * to send order details message to Kafka topic
     */
    public String sendMessage(Customer message) {
        log.info(String.format("$$ -> Producing message --> %s", message.toString()));
        kafkaTemplate.send(topic, message);
        return "$$ -> Produced message -->" + message;
    }
}
