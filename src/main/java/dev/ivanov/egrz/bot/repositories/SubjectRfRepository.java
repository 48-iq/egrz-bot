package dev.ivanov.egrz.bot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.ivanov.egrz.bot.entities.SubjectRf;

@Repository
public interface SubjectRfRepository extends JpaRepository<SubjectRf, Integer> {
  
  @Query("SELECT s FROM SubjectRf s JOIN s.users u WHERE u.chatId = :chatId")
  List<SubjectRf> findByUserChatId(String chatId);

}
