package com.stackroute.kafkaproducer.config;

import com.stackroute.kafkaproducer.domain.Customer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


/**
 * Configure all the beans as described below to send messages to Kafka Message broker.
 * Add appropriate annotation to this Bean Configuration class
 */
@Configuration
public class KafkaProducerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Create a Bean of type Map<String, Object> containing the configuration
     * of Kafka Producer. The configuration should include bootstrap server, message key and value serializers
     * message key should be of type String and Value should be of type Json containing Order details
     * present in OrderDto
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> configuration = new HashMap<>();
        configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return configuration;
    }

    /**
     * Create a Bean of ProducerFactory<String, OrderDto> using the above
     * producer configuration
     */
    @Bean
    public ProducerFactory<String, Customer> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Create a bean of KafkaTemplate with the above ProducerFactory
     */
    @Bean
    public KafkaTemplate<String, Customer> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
