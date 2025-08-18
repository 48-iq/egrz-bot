package dev.ivanov.egrz.bot.handlers;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.services.KeywordService;
import dev.ivanov.egrz.bot.services.TextService;
import dev.ivanov.egrz.bot.services.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeywordsHandler implements Handler {

  private final BotContainer botContainer;

  private final TextService textService;

  private final KeywordService keywordService;

  private final UserService userService;

  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    String chatId = update.getMessage().getChatId().toString();
    if (userService.getState(chatId).type.equals("input"))
      return false;
    return update.getMessage().getText().equals("/keywords");
  }

  @Override
  public void handle(Update update) throws TelegramApiException {
    String chatId = update.getMessage().getChatId().toString();
    userService.saveUserIfNotExist(chatId);
    StringBuilder messageTextBuilder = new StringBuilder();
    messageTextBuilder.append(textService.getText("keywords"));
    messageTextBuilder.append("\n\n");
    List<String> userKeywords = keywordService.getKeywordsByChatId(chatId);
    for(int i = 0; i < userKeywords.size(); i++) {
      String keyword = userKeywords.get(i);
      messageTextBuilder.append(keyword);
      if (i + 1 < userKeywords.size())
        messageTextBuilder.append(",");
      messageTextBuilder.append("\n");
    }
    SendMessage sendMessage = new SendMessage(
      chatId, 
      messageTextBuilder.toString()
    );
    botContainer.getBot().execute(sendMessage);
  }
  
}
