## Problem Statement: Kafka Producer Microservice

## Problem Statement
  - kafka-producer: This service sends the data received by the restful controller to kafka in a particular topic.
    *  this service receives data through controller and sends the data through "Producer" service class,
    *  the "KafkaTemplate" is used to send data to kafka which is configured using the config class KafkaProducerConfig
    *  the topic, address, groupId for configuration are provided in "application.yml"

## Running the application locally after cloning
    > After implementing the requirements, execute the following maven command in the parent/root project

            mvn clean package
    
    > Start Kafka. Instruction for installation and starting these are provided in below section

    > The Service KafkaProducerApplication have to be started
    
    > Run the following command `bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic customer_details --from-beginning`

## Following software needs to be available/installed in the local environment
**Kafka**
     > Refer the steps provided at the below url to install and start apache kafka

         https://kafka.apache.org/quickstart

## Instructions
  - Take care of whitespace/trailing whitespace
  - Do not change the provided class/method names unless instructed
  - Ensure your code compiles without any errors/warning/deprecations
  - Follow best practices while coding
