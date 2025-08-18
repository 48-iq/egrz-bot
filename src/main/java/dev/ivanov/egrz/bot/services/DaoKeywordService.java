package dev.ivanov.egrz.bot.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.ivanov.egrz.bot.entities.Keyword;
import dev.ivanov.egrz.bot.entities.User;
import dev.ivanov.egrz.bot.repositories.KeywordRepository;
import dev.ivanov.egrz.bot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaoKeywordService implements KeywordService {

  private final KeywordRepository keywordRepository;

  private final UserRepository userRepository;

  @Override
  @Transactional
  public List<String> getKeywordsByChatId(String chatId) {
    List<Keyword> keywords = keywordRepository.findByUserChatId(chatId);

    return keywords.stream()
      .map(Keyword::getKeyword)
      .toList();
  }

  @Override
  @Transactional
  public void setKeywordsForChatId(String chatId, List<String> keywords) {
  
    User user = userRepository.findByChatId(chatId).orElseThrow(
      () -> new IllegalArgumentException("User with chatId " + chatId + " not found")
    );

    List<Keyword> newKeywords = new ArrayList<>();

    for (String keywordString: keywords) {
      Keyword keyword = new Keyword();
      keyword.setKeyword(keywordString);
      keyword.setUser(user);
      newKeywords.add(keyword);
    }

    keywordRepository.deleteByUserId(user.getId());
    keywordRepository.saveAll(newKeywords);

  }
  
}
