package com.hkmc.upsourcewehbook.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReviewDetails {
  private String projectId;
  private String reviewId;

  @Builder
  public ReviewDetails(String projectId, String reviewId) {
    this.projectId = projectId;
    this.reviewId = reviewId;
  }

}
