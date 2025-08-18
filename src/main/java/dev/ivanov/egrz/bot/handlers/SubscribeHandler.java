package dev.ivanov.egrz.bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.services.TextService;
import dev.ivanov.egrz.bot.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscribeHandler implements Handler {

  private final UserService userService;

  private final TextService textService;

  private final BotContainer botContainer;

  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    String chatId = update.getMessage().getChatId().toString();
    if (userService.getState(chatId).type.equals("input"))
      return false;
    return update.getMessage().getText().equals("/subscribe");
  }

  @Override
  public void handle(Update update) throws TelegramApiException {
    String chatId = update.getMessage().getChatId().toString();
    userService.saveUserIfNotExist(chatId); 
    userService.setSubscribed(chatId, true);
    String messageText = textService.getText("subscribe");
    SendMessage sendMessage = new SendMessage(chatId, messageText);
    botContainer.getBot().execute(sendMessage);
  }
  
}
