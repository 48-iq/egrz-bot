package dev.ivanov.egrz.bot.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ivanov.egrz.bot.entities.SubjectRf;
import dev.ivanov.egrz.bot.repositories.SubjectRfRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectsRfInitService {

  @Value("classpath:subjects.json")
  private Resource subjectsJson;

  private final SubjectRfRepository subjectRfRepository;
  
  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public void init() {

    String sqlInit = "CREATE TABLE IF NOT EXISTS app_data_migrations (\r\n" + //
            "    id VARCHAR(255) PRIMARY KEY,\r\n" + //
            "    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\r\n" + //
            ");";
    jdbcTemplate.execute(sqlInit);
    
    String sqlSelect = "SELECT COUNT(*) FROM app_data_migrations WHERE id = 'subjects'";

    Integer count = jdbcTemplate.queryForObject(sqlSelect, Integer.class);
    if (count == null) {
      throw new RuntimeException("Error writing subjects.json in db (count is null)");
    }
    if (count == 0) {
      ObjectMapper objectMapper = new ObjectMapper();
      try (InputStream inputStream = subjectsJson.getInputStream()) {
        
        Map<String, Integer> subjects = objectMapper.readValue(
          inputStream, 
          new TypeReference<Map<String, Integer>>(){}
        );

        List<SubjectRf> subjectEntities = subjects.entrySet().stream()
          .map(entry -> SubjectRf.builder()
            .name(entry.getKey())
            .code(entry.getValue())
            .build()
          )
          .toList();

        subjectRfRepository.saveAll(subjectEntities);

      } catch (IOException e) {
        log.error("Error reading subjects.json", e);
        throw new RuntimeException(e);
      }

      String sqlInsert = "INSERT INTO app_data_migrations (id) VALUES ('subjects')";
      jdbcTemplate.update(sqlInsert);
    }
  }

  
}
