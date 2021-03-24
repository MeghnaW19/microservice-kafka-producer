package com.stackroute.kafkaproducer.commander.integrationtest;

import com.stackroute.kafkaproducer.domain.Customer;
import com.stackroute.kafkaproducer.service.Producer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(partitions = 1, controlledShutdown = false, topics = {"customer_details"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@SpringBootTest
@DirtiesContext
public class ProducerServiceEmbeddedKafkaITTests {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ProducerServiceEmbeddedKafkaITTests.class);

    private static String TOPIC = "customer_details";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private Producer producer;

    private KafkaMessageListenerContainer<String, Customer> container;

    private BlockingQueue<ConsumerRecord<String, Customer>> records;

    @BeforeEach
    public void setUp() {
        records = new LinkedBlockingQueue<>();
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("consumer", "false",
                        embeddedKafkaBroker);

        DefaultKafkaConsumerFactory<String, Customer> consumerFactory =
                new DefaultKafkaConsumerFactory<String, Customer>(
                        consumerProperties, new StringDeserializer(), new JsonDeserializer<>(Customer.class, false));

        ContainerProperties containerProperties =
                new ContainerProperties(TOPIC);
        container = new KafkaMessageListenerContainer<>(consumerFactory,
                containerProperties);
        container
                .setupMessageListener((MessageListener<String, Customer>) record -> {
                    LOGGER.debug("test-listener received message='{}'",
                            record.toString());
                    records.add(record);
                });
        container.start();
        ContainerTestUtils.waitForAssignment(container,
                embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterEach
    public void tearDown() {
        container.stop();
    }

    @Test
    public void givenCustomerWhenSentToKafkaTopicThenReceivedSuccessfully() throws InterruptedException {

        Customer customer = new Customer("Nick", "Jonas", "Male", "nick@email.com");

        producer.sendMessage(customer);
        ConsumerRecord<String, Customer> receivedMessage =
                records.poll(10, TimeUnit.SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.value().getEmail()).isEqualTo(customer.getEmail());
    }
}