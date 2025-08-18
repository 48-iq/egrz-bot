package dev.ivanov.egrz.bot.entities;

import java.time.LocalDateTime;
import java.util.List;

import dev.ivanov.egrz.bot.state.States;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String chatId;

  Boolean subscribed;

  @Enumerated(EnumType.STRING)
  States chatState;

  LocalDateTime subscribedAt;

  @ManyToMany
  @JoinTable(
    name = "users_subjects_rf",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "subject_rf_code")
  )
  List<SubjectRf> subjects;

  @OneToMany(mappedBy = "user")
  List<Keyword> keywords;

}
