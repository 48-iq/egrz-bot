package dev.ivanov.egrz.bot.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.ivanov.egrz.bot.dto.OpinionDto;
import dev.ivanov.egrz.bot.dto.PublicRegistrationBookResponse;
import dev.ivanov.egrz.bot.entities.Opinion;
import dev.ivanov.egrz.bot.repositories.OpinionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaoOpinionService implements OpinionService{

  private final RestTemplate restTemplate;

  private final OpinionRepository opinionRepository;

  @Override 
  @Transactional
  public void updateOpinions() {

    LocalDate yesterday = LocalDate.now(ZoneId.of("+04:00:00")).minusDays(1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = yesterday.format(formatter);
    
    int skip = 0;
    List<Opinion> opinionEntities = new ArrayList<>();
    List<OpinionDto> opinions = new ArrayList<>();
    do {

      String url = String.format("/PublicRegistrationBook?" + //
              "$filter=(" + //
              "WorkType eq 'Строительство' " + //
              "and date(ExpertiseDate) ge %s " + //
              "and date(ExpertiseDate) le %sT23:59:59.999Z" + //
              ")&$skip=%d", formattedDate, formattedDate, skip);
      ResponseEntity<PublicRegistrationBookResponse> responseEntity = 
          restTemplate.getForEntity(url, PublicRegistrationBookResponse.class);
      if (responseEntity.getStatusCode().is2xxSuccessful()) {

        PublicRegistrationBookResponse response = responseEntity.getBody();
        if (response == null) {
          throw new RuntimeException("Response is null");
        }

        
        opinions = response.getValue();
        for (OpinionDto opinion : opinions) {
          Opinion opinionEntity = opinion.mapToEntity();
          opinionEntities.add(opinionEntity);
        }
      }
      skip += 10;
    } while(!opinions.isEmpty());

    log.info("{} opinions was updated", opinionEntities.size());

    opinionRepository.deleteAll();
    opinionRepository.saveAll(opinionEntities);
  }

  @Override
  public List<Opinion> getAllOpinions() {
    return opinionRepository.findAll();
  }

  @Override
  public List<Opinion> getOpinionsByKeywordsAndSubjects(
      List<String> keywords, 
      List<Integer> subjects, 
      List<Opinion> opinions
  ) {
    
    List<Opinion> result = new ArrayList<>();

    Class<Opinion> opinionClass = Opinion.class;

    Method[] opinionMethods = opinionClass.getDeclaredMethods();

    for (Opinion opinion: opinions) {
      if (subjects.contains(opinion.getSubjectRfCode()) || subjects.isEmpty()) {
        if (keywords.isEmpty()) {
          result.add(opinion);
          continue;
        }
        StringBuilder textBuilder = new StringBuilder();
        for (Method method: opinionMethods) {
          if (method.getName().startsWith("get")) {
            try {
              Object methodReturn = method.invoke(opinion);
              if (methodReturn != null)
                textBuilder.append(methodReturn.toString().toLowerCase().trim());
            } catch (IllegalAccessException|InvocationTargetException e) {
              log.error("reflection method call exception", e);
            }
          }
        }
        String text = textBuilder.toString();
        for (String keyword: keywords) {
          if (text.matches(String.format(".*%s.*", keyword))) {
            result.add(opinion);
            break;
          }
        }
      } 
    }
    
    return result;
  }

  
} 
