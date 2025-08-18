package dev.ivanov.egrz.bot.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.ivanov.egrz.bot.services.SubjectsRfInitService;

@Configuration
public class SubjectsRfInitConfig {
  
  @Bean
  public CommandLineRunner subjectRfInit(SubjectsRfInitService subjectRfInitService) {
    return args -> {
      subjectRfInitService.init();
    };
  }
}
