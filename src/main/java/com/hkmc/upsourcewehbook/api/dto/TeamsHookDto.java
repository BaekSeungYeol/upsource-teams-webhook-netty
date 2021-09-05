package com.hkmc.upsourcewehbook.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeamsHookDto {

  private static final String MESSAGE_CARD = "MessageCard";

  private static final String CONTEXT = "http://schema.org/extensions";

  private static final String THEME_COLOR = "0076D7";

  @JsonProperty("@type")
  private String type;

  @JsonProperty("@context")
  private String context;

  private String themeColor;

  private String summary; // reviwer create, 등을 알려줄 예정

  private List<Sections> sections = new ArrayList<>();

  private List<PotentialAction> potentialAction = new ArrayList<>();

  public TeamsHookDto(String reviewTitle, Sections sections, PotentialAction potentialAction) {
    this(reviewTitle, sections);
    this.potentialAction.add(potentialAction);
  }

  public TeamsHookDto(String reviewTitle, Sections sections) {
    this.type = MESSAGE_CARD;
    this.context = CONTEXT;
    this.themeColor = THEME_COLOR;
    this.summary = reviewTitle;
    this.sections.add(sections);
  }

}
