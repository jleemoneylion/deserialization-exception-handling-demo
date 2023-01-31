package com.example.poisonpillexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DataConsume {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataConsume.class);

  @KafkaListener(topics = "example-topic", groupId = "example-consumer")
  public void consume(ExampleDto dto) {
    LOGGER.info("Consumed: " + dto);
  }
}
