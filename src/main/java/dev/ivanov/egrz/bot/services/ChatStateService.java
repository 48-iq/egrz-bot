package dev.ivanov.egrz.bot.services;

import dev.ivanov.egrz.bot.state.States;

public interface ChatStateService {
  void setState(String chatId, String state);
  States getState(String chatId);
}
