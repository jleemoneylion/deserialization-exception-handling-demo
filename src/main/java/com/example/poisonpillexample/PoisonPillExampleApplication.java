package com.example.poisonpillexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PoisonPillExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(PoisonPillExampleApplication.class, args);
  }

}
