package dev.ivanov.egrz.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Keyword {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  
  private String id;

  private String keyword;
  
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
