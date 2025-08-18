package dev.ivanov.egrz.bot.services;

import static dev.ivanov.egrz.bot.state.States.MAIN;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.ivanov.egrz.bot.entities.User;
import dev.ivanov.egrz.bot.repositories.UserRepository;
import dev.ivanov.egrz.bot.state.States;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DaoUserService implements UserService {

  private final UserRepository userRepository;

  @Override
  public void saveUserIfNotExist(String chatId) {
    Optional<User> user = userRepository.findByChatId(chatId);
    if (user.isEmpty()) {
      User newUser = User.builder()
      .chatId(chatId)
      .subscribed(true)
      .chatState(MAIN)
      .build();
      userRepository.save(newUser);
    }
  }

  @Override
  public boolean isSubscribed(String chatId) {
    User user = userRepository.findByChatId(chatId)
      .orElseThrow(() -> new IllegalArgumentException("User with chatId " + chatId + " not found"));
    return user.getSubscribed();
  }

  @Override
  public void setSubscribed(String chatId, boolean subscribed) {
    User user = userRepository.findByChatId(chatId)
      .orElseThrow(() -> new IllegalArgumentException("User with chatId " + chatId + " not found"));
    user.setSubscribed(subscribed);
  }

  @Override
  public States getState(String chatId) {
    Optional<User> user = userRepository.findByChatId(chatId);
    if (user.isEmpty())
      return States.MAIN;
    return user.get().getChatState();
  }

  @Override
  @Transactional
  public void setState(String chatId, States state) {
    User user = userRepository.findByChatId(chatId)
      .orElseThrow(() -> new IllegalArgumentException("User with chatId " + chatId + " not found"));
    user.setChatState(state);
    userRepository.save(user);
  }
  
}
