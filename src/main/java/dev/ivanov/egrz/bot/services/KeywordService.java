package dev.ivanov.egrz.bot.services;

import java.util.List;

public interface KeywordService {
  List<String> getKeywordsByChatId(String chatId);
  void setKeywordsForChatId(String chatId, List<String> keywords);
}
