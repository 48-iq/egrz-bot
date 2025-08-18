package dev.ivanov.egrz.bot.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.ivanov.egrz.bot.entities.User;
import dev.ivanov.egrz.bot.repositories.UserRepository;
import dev.ivanov.egrz.bot.state.States;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DaoChatStateService implements ChatStateService {

  private final UserRepository userRepository;

  @Override
  public void setState(String chatId, String state) {
    Optional<User> user = userRepository.findByChatId(chatId);
    if (user.isPresent())
      user.get().setChatState(States.valueOf(state));
    else 
      throw new IllegalArgumentException("User with chatId " + chatId + " not found");
  }

  @Override
  public States getState(String chatId) {
    User user = userRepository.findByChatId(chatId)
      .orElseThrow(() -> new IllegalArgumentException("User with chatId " + chatId + " not found"));
    return user.getChatState();
  }
  
}
