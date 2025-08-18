package dev.ivanov.egrz.bot.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultTextService implements TextService {

  private final ResourceLoader resourceLoader;

  private Map<String, String> texts = new HashMap<>();


  @PostConstruct
  public void init() {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try (InputStream is = resourceLoader.getResource("classpath:/texts.yaml").getInputStream()) {
      Map<String, String> texts = mapper.readValue(is, new TypeReference<>() {});
      this.texts = texts;
      log.info("{} texts was loaded", texts.size());
    } catch (IOException e) {
      log.error("Error reading texts.yaml", e);
    }

  }

  @Override
  public String getText(String key) {
    String text = texts.get(key);
    if (text == null)
      throw new IllegalArgumentException("Text with key " + key + " not found");
    log.debug("Text with key {} was found", key);
    return text;
  }


  
}
