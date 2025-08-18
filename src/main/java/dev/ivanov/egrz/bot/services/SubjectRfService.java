package dev.ivanov.egrz.bot.services;

import java.util.List;

import dev.ivanov.egrz.bot.entities.SubjectRf;

public interface SubjectRfService {
  List<SubjectRf> getAllSubjectsRf();
  List<SubjectRf> getSubjectsRfByChatId(String chatId);
  void setSubjectsRfForChatId(String chatId, List<Integer> subjects);
}
