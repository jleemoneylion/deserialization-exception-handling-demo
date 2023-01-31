package com.example.poisonpillexample;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataGen {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataGen.class);
  private final KafkaTemplate<String, ExampleDto> kafkaTemplate;
  private final KafkaTemplate<String, String> faultInjectingKafkaTemplate;

  public DataGen(final KafkaTemplate<String, ExampleDto> kafkaTemplate,
                 final KafkaTemplate<String, String> faultInjectingKafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    this.faultInjectingKafkaTemplate = faultInjectingKafkaTemplate;
  }

  @Scheduled(fixedRate = 1000)
  public void generateValidData() {
    LOGGER.info("Sending message");
    final var f = kafkaTemplate.send("example-topic", new ExampleDto(Instant.now().toString()));
    f.whenComplete((result, error) -> {
      if (error != null) {
        LOGGER.info("Error sending message: " + error.getMessage());
      } else {
        LOGGER.info("Message sent: " + result.getProducerRecord().value());
      }
    });
  }

  @Scheduled(fixedRate = 10_000)
  public void generateInvalidData() {
    LOGGER.info("Sending poison pill");
    final var f = faultInjectingKafkaTemplate.send("example-topic", "poison pill");
    f.whenComplete((result, error) -> {
      if (error != null) {
        LOGGER.info("Error sending poison pill: " + error.getMessage());
      } else {
        LOGGER.info("Poison pill sent: " + result.getProducerRecord().value());
      }
    });
  }
}
