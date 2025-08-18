package dev.ivanov.egrz.bot.services;

import dev.ivanov.egrz.bot.state.States;

public interface UserService {
  
  void saveUserIfNotExist(String chatId);

  boolean isSubscribed(String chatId);

  void setSubscribed(String chatId, boolean subscribed);

  States getState(String chatId);

  void setState(String chatId, States state);

  
}
