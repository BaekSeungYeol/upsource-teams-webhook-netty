package com.hkmc.upsourcewehbook.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TargetURL {

  private String os;

  private String uri;

  public TargetURL(String uri) {
    this.os = "default";
    this.uri = uri;
  }

}
