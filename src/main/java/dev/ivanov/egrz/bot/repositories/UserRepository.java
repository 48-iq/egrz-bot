package dev.ivanov.egrz.bot.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.ivanov.egrz.bot.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByChatId(String chatId);
  @Query("SELECT u FROM User u WHERE u.subscribed = true")
  Page<User> findAllSubscribed(Pageable pageable);
}
