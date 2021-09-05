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
public class Action {

  @JsonProperty("@type")
  private String type;

  private String name;

  private List<TargetURL> targets = new ArrayList<>();

  public Action() {/* empty */ }

  public Action(TargetURL url) {
    this.type = "OpenUri";
    this.name = "Comment on a Review";
    this.targets.add(url);
  }

}
