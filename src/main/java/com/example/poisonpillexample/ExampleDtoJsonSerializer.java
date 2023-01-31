package com.example.poisonpillexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class ExampleDtoJsonSerializer implements Serializer<ExampleDto> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public byte[] serialize(final String topic, final ExampleDto data) {
    try {
      return objectMapper.writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
