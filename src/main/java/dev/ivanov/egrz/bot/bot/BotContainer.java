package dev.ivanov.egrz.bot.bot;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.ivanov.egrz.bot.handlers.Handler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Содержит экземпляр long polling telegram бота,
 * вызывает обработчиков в зависимости от Update,
 * необходим для внедрения экземпляра бота в обработчики,
 * т.к. бот реализует интерфейс с финальными методами 
 * и его bean создается неправильно
 */
@Component
@Slf4j
public class BotContainer {
  
  @Getter
  private TelegramLongPollingBot bot;

  private ExecutorService executorService;

  List<Handler> handlers;

  @Lazy
  @Autowired
  public void setHandlers(List<Handler> handlers) {
    this.handlers = handlers;
  }

  public BotContainer(
    @Value("${bot.token}") String token,
    @Value("${bot.username}") String username,
    @Value("${bot.threadPoolSize}") int threadPoolSize
  ) {

    executorService = Executors.newFixedThreadPool(threadPoolSize);

    bot = new TelegramLongPollingBot(token) {

      @Override
      public void onUpdateReceived(Update update) {
        executorService.submit(() -> {
          try {
            for (Handler handler : handlers) {
              if (handler.match(update)) {
                handler.handle(update);
                break;
              }
            }
          } catch (Exception e) {
            log.error("Error handling update", e);
            throw new RuntimeException(e);
          }
        });
      }

      @Override
      public String getBotUsername() {
        return username;
      }
      
    };
  }
}
