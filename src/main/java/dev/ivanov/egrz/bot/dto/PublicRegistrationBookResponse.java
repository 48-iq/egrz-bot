package dev.ivanov.egrz.bot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicRegistrationBookResponse {
  
  private List<OpinionDto> value;
}
