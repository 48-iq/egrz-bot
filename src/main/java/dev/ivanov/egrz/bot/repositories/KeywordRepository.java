package dev.ivanov.egrz.bot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.ivanov.egrz.bot.entities.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, String> {
  @Query("SELECT k FROM Keyword k JOIN k.user u WHERE u.chatId = :chatId")
  List<Keyword> findByUserChatId(String chatId);
    void deleteByUserId(String userId);
}
