package com.example.poisonpillexample;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

public class PoisonRecoveryProvider implements Function<FailedDeserializationInfo, ExampleDto> {
  private static final Logger LOGGER = LoggerFactory.getLogger(PoisonRecoveryProvider.class);

  @Override
  public ExampleDto apply(final FailedDeserializationInfo failedDeserializationInfo) {
    final var exception = failedDeserializationInfo.getException();
    final var dataAsString = new String(failedDeserializationInfo.getData());
    LOGGER.info("Failed to deserialize: \"" + dataAsString + "\" with exception: " + exception.getMessage());
    return new ExampleDto("recovered");
  }
}
