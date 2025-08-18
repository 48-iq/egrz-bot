package dev.ivanov.egrz.bot.state;

public enum States {
  
  MAIN("default"),
  KEYWORDS_INPUT("input"),
  SUBJECTS_INPUT("input");

  public final String type;

  private States(String type) {
    this.type = type;
  }
  
}
