package dev.ivanov.egrz.bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.services.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {

  private final BotContainer botContainer;

  private final UserService userService;

  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    if (userService.getState(update.getMessage().getChatId().toString()).type.equals("input"))
      return false;
    Message message = update.getMessage();
    return message.getText().equals("/start");
  }


  @Override
  public void handle(Update update) throws TelegramApiException {
    userService.saveUserIfNotExist(update.getMessage().getChatId().toString());
    SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), "Bot started");
    botContainer.getBot().execute(sendMessage);
  }

  
}
