package com.hkmc.upsourcewehbook.models.impl;

import java.util.Arrays;
import java.util.List;

import com.hkmc.upsourcewehbook.api.dto.Action;
import com.hkmc.upsourcewehbook.api.dto.Contents;
import com.hkmc.upsourcewehbook.api.dto.PotentialAction;
import com.hkmc.upsourcewehbook.api.dto.Sections;
import com.hkmc.upsourcewehbook.api.dto.TargetURL;
import com.hkmc.upsourcewehbook.api.dto.TeamsHookDto;
import com.hkmc.upsourcewehbook.models.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.jayway.jsonpath.JsonPath.parse;

@Getter
@Setter
@ToString
public class LabelReview implements Review {

  private String reviewTitle;

  private String projectId;

  private String author;

  private String reviewId;

  // NewRevisionEventBean
  public LabelReview(String json) {
    this.reviewTitle = "리뷰가 생성되었습니다.";
    this.projectId = parse(json).read("$.projectId", String.class);
    this.author = parse(json).read("$.data.actor.userName", String.class);
    this.reviewId = parse(json).read("$.data.reviewId", String.class);
  }

  public static boolean checkLabelAdded(String json) {
    String checkLabelName = parse(json).read("$.data.labelName", String.class);
    String checkWasAdded = parse(json).read("$.data.wasAdded", String.class);

    return checkLabelName.equals("ready for review") && checkWasAdded.equals("true");
  }

  public TeamsHookDto getRevisionTeamsDto(String names) {
    return new TeamsHookDto(this.reviewTitle, this.getRevisionSections(names), this.getRevisionPotentialAction());
  }

  public Action getRevisionAction() {
    return new Action(this.getRevisionTargetURL());
  }

  public PotentialAction getRevisionPotentialAction() {
    return new PotentialAction(getRevisionAction());
  }

  public TargetURL getRevisionTargetURL() {
    return new TargetURL(combineTargetURL(this.projectId, this.reviewId));
  }

  public Sections getRevisionSections(String names) {
    return new Sections(this.reviewTitle, this.projectId, getFacts(names));
  }

  public List<Contents> getFacts(String users) {
    return Arrays.asList(
      new Contents("작성자", this.author),
      new Contents("리뷰 ID", this.reviewId),
      new Contents("리뷰어", users),
      new Contents("작성 날짜", getFormattedDate()));
  }

}

