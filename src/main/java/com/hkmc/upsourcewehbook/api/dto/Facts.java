package com.hkmc.upsourcewehbook.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Facts {

  private List<Contents> facts;

  public Facts(List<Contents> facts) {
    this.facts = facts;
  }

}
