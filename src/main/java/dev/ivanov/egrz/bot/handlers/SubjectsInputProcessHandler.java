package dev.ivanov.egrz.bot.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.services.SubjectRfService;
import dev.ivanov.egrz.bot.services.TextService;
import dev.ivanov.egrz.bot.services.UserService;
import dev.ivanov.egrz.bot.state.States;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubjectsInputProcessHandler implements Handler{
  
  private final BotContainer botContainer;

  private final SubjectRfService subjectRfService;

  private final UserService userService;

  private final TextService textService;
  
  @Override
  public boolean match(Update update) {
    if (!update.hasMessage()) return false;
    States state = userService.getState(update.getMessage().getChatId().toString());
    return state.type.equals("input") && state.equals(States.SUBJECTS_INPUT);
  }

  @Override
  public void handle(Update update) throws TelegramApiException {
    String updateText = update.getMessage().getText();
    List<Integer> parsedSubjectCodes = new ArrayList<>();
    if (!updateText.equals("-")) {
      List<String> splitedSubjectCodes = Arrays.asList(updateText.split(","));
      log.info(splitedSubjectCodes.toString());
      for (String subjectCode: splitedSubjectCodes) {
        if (!subjectCode.trim().matches("^[0-9]+$")) continue;
        parsedSubjectCodes.add(Integer.parseInt(subjectCode.trim()));
      }
    }
    subjectRfService.setSubjectsRfForChatId(update.getMessage().getChatId().toString(), parsedSubjectCodes);
    userService.setState(update.getMessage().getChatId().toString(), States.MAIN);
    String messageText = textService.getText("successDataUpdate");
    SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), messageText);
    botContainer.getBot().execute(sendMessage);
  }
  
}
