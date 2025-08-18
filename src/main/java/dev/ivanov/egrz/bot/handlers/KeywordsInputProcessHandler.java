package dev.ivanov.egrz.bot.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.services.KeywordService;
import dev.ivanov.egrz.bot.services.TextService;
import dev.ivanov.egrz.bot.services.UserService;
import dev.ivanov.egrz.bot.state.States;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeywordsInputProcessHandler implements Handler{

  private final UserService userService;

  private final TextService textService;

  private final KeywordService keywordService;

  private final BotContainer botContainer;

  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    String chatId = update.getMessage().getChatId().toString();
    States chatState = userService.getState(chatId);
    return chatState.type.equals("input")
      && chatState.equals(States.KEYWORDS_INPUT);
  }

  @Override
  public void handle(Update update) throws TelegramApiException {
    
    String chatId = update.getMessage().getChatId().toString();

    userService.setState(chatId, States.MAIN);

    String updateText = update.getMessage().getText();

    List<String> keywords = new ArrayList<>();
    
    if (!updateText.equals("-"))
      keywords = Arrays.asList(updateText.split(",")).stream()
        .map(s -> s.trim())
        .toList();

    keywordService.setKeywordsForChatId(chatId, keywords);
      
    String textMessage = textService.getText("successDataUpdate");
    SendMessage sendMessage = new SendMessage(chatId, textMessage);
    botContainer.getBot().execute(sendMessage);
  }
  
}
