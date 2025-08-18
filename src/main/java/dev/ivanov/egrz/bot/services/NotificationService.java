package dev.ivanov.egrz.bot.services;

public interface NotificationService {
  void sendNotification(String chatId, String message);
  void sendNotificationsToAll(String message);
}
