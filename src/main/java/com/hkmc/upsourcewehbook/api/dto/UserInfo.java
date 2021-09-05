package com.hkmc.upsourcewehbook.api.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserInfo {
  private List<String> ids = new ArrayList<>();

  @Builder
  public UserInfo(List<String> ids) {
    this.ids = ids;
  }

}
