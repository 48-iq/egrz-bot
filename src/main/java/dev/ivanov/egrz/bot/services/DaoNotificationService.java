package dev.ivanov.egrz.bot.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaoNotificationService implements NotificationService {

  private final UserRepository userRepository;

  private final BotContainer botContainer;

  @Override
  public void sendNotification(String chatId, String message) {
    SendMessage sendMessage = new SendMessage(chatId, message);
    try {
      botContainer.getBot().execute(sendMessage);
    } catch (TelegramApiException e) {
      log.error("Error sending notification", e);
    }
  }

  @Override
  public void sendNotificationsToAll(String message) {
    Integer page = 0;
    Integer pageSize = 100;
    while (true) {
      var users = userRepository.findAll(PageRequest.of(page, pageSize));
      users.forEach(user -> sendNotification(user.getChatId(), message));
      if (users.getTotalPages() <= page + 1) break;
      page++;
    }
  }
  
}
