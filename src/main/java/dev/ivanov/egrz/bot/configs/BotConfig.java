package dev.ivanov.egrz.bot.configs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import dev.ivanov.egrz.bot.bot.BotContainer;
import dev.ivanov.egrz.bot.services.TextService;

@Configuration
public class BotConfig {
  

  /**
   * Привязывает экземпляр бота к TelegramBotsApi
   * !!! бот не запустится без TelegramBotsApi !!!
   */
  @Bean
  public TelegramBotsApi telegramBotsApi(BotContainer botContainer,
    TextService textService
  ) throws TelegramApiException{
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    telegramBotsApi.registerBot(botContainer.getBot());
    SetMyCommands setMyCommands = new SetMyCommands();
    
    setMyCommands.setCommands(List.of(
      new BotCommand("/subscribe", textService.getText("subscribeCommand")),
      new BotCommand("/unsubscribe", textService.getText("unsubscribeCommand")),
      new BotCommand("/keywords", textService.getText("keywordsCommand")),
      new BotCommand("/subjects", textService.getText("subjectsCommand")),
      new BotCommand("/set_keywords", textService.getText("setKeywordsCommand")),
      new BotCommand("/set_subjects", textService.getText("setSubjectsCommand"))
    ));
    botContainer.getBot().execute(setMyCommands);
    return telegramBotsApi;
  }
}
