package com.hkmc.upsourcewehbook.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PotentialAction {

  @JsonProperty("@type")
  private String type;

  private String name;

  private List<Action> actions = new ArrayList<>();

  public PotentialAction(Action action) {
    this.type = "ActionCard";
    this.name = "Comment on a Review";
    this.actions.add(action);
  }

}
