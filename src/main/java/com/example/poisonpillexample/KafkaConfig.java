package com.example.poisonpillexample;

import java.util.HashMap;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

@Configuration
@EnableKafka
public class KafkaConfig {
  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    var configs = new HashMap<String, Object>();
    configs.put("bootstrap.servers", bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic exampleTopic() {
    return new NewTopic("example-topic", 1, (short) 1);
  }

  @Bean
  @Primary
  public ProducerFactory<String, ExampleDto> producerFactory() {
    var configs = new HashMap<String, Object>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ExampleDtoJsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  @Primary
  public KafkaTemplate<String, ExampleDto> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ProducerFactory<String, String> faultInjectingProducerFactory() {
    var configs = new HashMap<String, Object>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  public KafkaTemplate<String, String> faultInjectingKafkaTemplate() {
    return new KafkaTemplate<>(faultInjectingProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, ExampleDto> consumerFactory() {
    var configs = new HashMap<String, Object>();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
    configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ExampleDtoJsonDeserializer.class);
    configs.put(ErrorHandlingDeserializer.VALUE_FUNCTION, PoisonRecoveryProvider.class);
    return new DefaultKafkaConsumerFactory<>(configs);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ExampleDto> kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, ExampleDto>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
