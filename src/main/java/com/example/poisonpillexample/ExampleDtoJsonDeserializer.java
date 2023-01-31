package com.example.poisonpillexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.common.serialization.Deserializer;

public class ExampleDtoJsonDeserializer implements Deserializer<ExampleDto> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public ExampleDto deserialize(final String topic, final byte[] data) {
    try {
      return objectMapper.readValue(data, ExampleDto.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
