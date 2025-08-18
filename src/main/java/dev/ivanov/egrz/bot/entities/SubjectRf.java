package dev.ivanov.egrz.bot.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subjects_rf")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SubjectRf {
  
  @Id
  Integer code;

  private String name;

  @ManyToMany(mappedBy = "subjects")
  private List<User> users;
}
