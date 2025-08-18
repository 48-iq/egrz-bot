package dev.ivanov.egrz.bot.handlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.services.TaskService;
import dev.ivanov.egrz.bot.services.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestHandler implements Handler {
  
  private final UserService userService;
  
  @Value("${bot.adminChatId}")
  private String adminChatId;

  private final TaskService taskService;

  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    String chatId = update.getMessage().getChatId().toString();
    if (!adminChatId.equals(chatId))
      return false;
    if (userService.getState(chatId).type.equals("input"))
      return false;
    return update.getMessage().getText().equals("/test");
  }

  @Override
  public void handle(Update update) throws TelegramApiException {
    String chatId = update.getMessage().getChatId().toString();
    userService.saveUserIfNotExist(chatId);
    taskService.doTask();
  }
  
}
