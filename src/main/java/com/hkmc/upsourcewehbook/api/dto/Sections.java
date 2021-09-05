package com.hkmc.upsourcewehbook.api.dto;

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
public class Sections {

  private static final String S3_LOGO_IMAGE = "https://shopbucket.s3.ap-northeast-2.amazonaws.com/picture1.PNG";

  private String activityTitle;

  private String activitySubtitle;

  private String activityImage;

  private List<Contents> facts = new ArrayList<>();

  private boolean markdown;

  public Sections(String reviewTitle, String projectId, List<Contents> facts) {
    this.activityTitle = reviewTitle;
    this.activitySubtitle = projectId;
    this.activityImage = S3_LOGO_IMAGE;
    this.facts.addAll(facts);
    this.markdown = true;
  }

}
