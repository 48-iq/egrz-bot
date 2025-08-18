package dev.ivanov.egrz.bot.services;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.ivanov.egrz.bot.entities.Opinion;
import dev.ivanov.egrz.bot.entities.SubjectRf;
import dev.ivanov.egrz.bot.entities.User;
import dev.ivanov.egrz.bot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class TaskService {
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  private final NotificationService notificationService;
  private final OpinionService opinionService;
  private final UserRepository userRepository;
  private final KeywordService keywordService;
  private final SubjectRfService subjectRfService;
  private final TextService textService;

  @Scheduled(cron = "0 0 12 * * ?")
  public void doTask() {
    executorService.submit(() -> {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        opinionService.updateOpinions();
        List<Opinion> opinions = opinionService.getAllOpinions();
        int page = 0; 
        int size = 100;
        int total = 1;
        do {
          Page<User> subscribedPage = userRepository.findAllSubscribed(PageRequest.of(page, size));
          total = subscribedPage.getTotalPages();
          for (User user: subscribedPage.getContent()) {
            List<Integer> userSubjects = subjectRfService.getSubjectsRfByChatId(user.getChatId())
              .stream()
              .map(SubjectRf::getCode)
              .toList();
            List<String> userKeywords = keywordService.getKeywordsByChatId(user.getChatId())
              .stream().map(s -> s.toLowerCase().trim())
              .toList();
            List<Opinion> userOpinions = opinionService.getOpinionsByKeywordsAndSubjects(
              userKeywords, 
              userSubjects, 
              opinions
            );
            StringBuilder messageTextBuilder = new StringBuilder();
            messageTextBuilder.append(textService.getText("notificationText"));
            messageTextBuilder.append("\n\n");
            if (!userOpinions.isEmpty()) {
              for (Opinion opinion: userOpinions) {
                messageTextBuilder.append(textService.getText("expertiseNumber"));
                messageTextBuilder.append(": ");
                messageTextBuilder.append(opinion.getExpertiseNumber());
                messageTextBuilder.append("\n");
                messageTextBuilder.append("https://egrz.ru/organisation/reestr/detail/" + opinion.getExpertiseNumber());
                messageTextBuilder.append("\n");
                messageTextBuilder.append(opinion.getSubjectRf());
                messageTextBuilder.append("\n");
                messageTextBuilder.append(opinion.getExpertiseDate().format(formatter));
                messageTextBuilder.append("\n\n");
              }
              notificationService.sendNotification(user.getChatId(), messageTextBuilder.toString()); 
            }
          }
          page++;
        } while (page < total);
      } catch (Exception e) {
        log.error("Error doing task", e);
      }
    });
  }
}
