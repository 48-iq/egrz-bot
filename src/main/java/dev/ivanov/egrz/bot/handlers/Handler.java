package dev.ivanov.egrz.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Handler {
  boolean match(Update update);
  void handle(Update update) throws TelegramApiException;
}
