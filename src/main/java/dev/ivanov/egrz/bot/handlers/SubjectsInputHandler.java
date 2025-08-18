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
import dev.ivanov.egrz.bot.state.States;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubjectsInputHandler implements Handler {

  private final BotContainer botContainer;

  private final UserService userService;

  private final TextService textService;

  private final SubjectRfService subjectRfService;

  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    if (userService.getState(update.getMessage().getChatId().toString()).type.equals("input"))
      return false;
    return update.getMessage().getText().equals("/set_subjects");
  }

  @Override
  public void handle(Update update) throws TelegramApiException {
    userService.saveUserIfNotExist(update.getMessage().getChatId().toString());
    List<SubjectRf> allSubjectsList = subjectRfService.getAllSubjectsRf();
    allSubjectsList.sort((s1, s2) -> s1.getCode() - s2.getCode());
    StringBuilder messageTextBuilder = new StringBuilder();
    messageTextBuilder.append(textService.getText("subjectsInput"));
    messageTextBuilder.append("\n\n");
    for (SubjectRf subjectRf: allSubjectsList) {
      messageTextBuilder.append(subjectRf.getName());
      messageTextBuilder.append("\n");
    }
    userService.setState(update.getMessage().getChatId().toString(), States.SUBJECTS_INPUT);
    SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), messageTextBuilder.toString());
    botContainer.getBot().execute(sendMessage);
  }
  
}
