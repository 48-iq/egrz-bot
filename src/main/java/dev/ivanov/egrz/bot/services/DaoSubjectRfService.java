package dev.ivanov.egrz.bot.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.ivanov.egrz.bot.entities.SubjectRf;
import dev.ivanov.egrz.bot.entities.User;
import dev.ivanov.egrz.bot.repositories.SubjectRfRepository;
import dev.ivanov.egrz.bot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaoSubjectRfService implements SubjectRfService {

  private final SubjectRfRepository subjectRfRepository;

  private final UserRepository userRepository;

  @Override
  public List<SubjectRf> getAllSubjectsRf() {
    return subjectRfRepository.findAll();
  }

  @Override
  @Transactional
  public List<SubjectRf> getSubjectsRfByChatId(String chatId) {
    return subjectRfRepository.findByUserChatId(chatId);
  }

  @Override
  @Transactional
  public void setSubjectsRfForChatId(String chatId, List<Integer> subjects) {
    User user = userRepository.findByChatId(chatId)
      .orElseThrow(() -> new IllegalArgumentException("User with chatId " + chatId + " not found"));
    
    log.info("subjects: {}", subjects);
    user.getSubjects().clear();;

    List<SubjectRf> allSubjects = getAllSubjectsRf();

    //validation
    for (Integer subjectCode: subjects) {
      for (SubjectRf subject: allSubjects) {
        if (subject.getCode().equals(subjectCode)) {
          user.getSubjects().add(subject);
        }
      }
    }

    userRepository.save(user);

  }
  
}
