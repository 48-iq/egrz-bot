package dev.ivanov.egrz.bot.handlers;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.entities.SubjectRf;
import dev.ivanov.egrz.bot.services.SubjectRfService;
import dev.ivanov.egrz.bot.services.TextService;
import dev.ivanov.egrz.bot.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubjectsHandler implements Handler{

  private final UserService userService;

  private final SubjectRfService subjectRfService;

  private final BotContainer botContainer;

  private final TextService textService;

  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    if (userService.getState(update.getMessage().getChatId().toString()).type.equals("input"))
      return false;
    return update.getMessage().getText().equals("/subjects");
  }

  @Override
  public void handle(Update update) throws TelegramApiException {
    userService.saveUserIfNotExist(update.getMessage().getChatId().toString());
    List<SubjectRf> subjectsList = subjectRfService.getSubjectsRfByChatId(update.getMessage().getChatId().toString());
    StringBuilder messageTextBuilder = new StringBuilder();
    messageTextBuilder.append(textService.getText("subjects"));
    messageTextBuilder.append("\n\n");
    for (SubjectRf subjectRf: subjectsList) {
      messageTextBuilder.append(subjectRf.getName());
      log.info(subjectRf.getName());
      messageTextBuilder.append("\n");
    }
    SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), messageTextBuilder.toString());
    botContainer.getBot().execute(sendMessage);
  }
  
}
