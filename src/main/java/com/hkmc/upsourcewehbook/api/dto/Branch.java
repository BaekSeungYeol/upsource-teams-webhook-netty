package com.hkmc.upsourcewehbook.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Branch {

  private String projectId;

  private String query;

  private int limit;

  @Builder
  public Branch(String projectId, String query, int limit) {
    this.projectId = projectId;
    this.query = query;
    this.limit = limit;
  }

}
