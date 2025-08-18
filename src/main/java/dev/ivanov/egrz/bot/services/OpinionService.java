package dev.ivanov.egrz.bot.services;

import java.util.List;

import dev.ivanov.egrz.bot.entities.Opinion;

public interface OpinionService {
  void updateOpinions();
  List<Opinion> getAllOpinions();
  List<Opinion> getOpinionsByKeywordsAndSubjects(List<String> keywords, List<Integer> subjects, List<Opinion> opinions);
}
