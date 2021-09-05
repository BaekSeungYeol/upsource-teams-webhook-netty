package com.hkmc.upsourcewehbook.models.impl;

import java.util.ArrayList;
import java.util.List;

import com.hkmc.upsourcewehbook.api.dto.Contents;
import com.hkmc.upsourcewehbook.api.dto.Sections;
import com.hkmc.upsourcewehbook.api.dto.TeamsHookDto;
import com.hkmc.upsourcewehbook.models.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.jayway.jsonpath.JsonPath.parse;

@Getter
@Setter
@ToString
public class AcceptReview implements Review {

  private final String reviewTitle;

  private final String reviewId;

  private final String projectId;

  private final String reviewer;

  // ParticipantStateChangedFeedEventBean
  public AcceptReview(String json) {
    this.reviewTitle = "해당 리뷰어가 Accept 하였습니다.";
    this.reviewId = parse(json).read("$.data.base.reviewId", String.class);
    this.projectId = parse(json).read("$.projectId", String.class);
    this.reviewer = parse(json).read("$.data.base.actor.userName", String.class);
  }

  public static boolean checkAcceptAdded(String json) {
    return "ParticipantStateChangedFeedEventBean".equals(parse(json).read("$.dataType", String.class))
             && parse(json).read("$.data.newState", Integer.class) == 2;
  }

  public TeamsHookDto getRevisionTeamsDto() {
    return new TeamsHookDto(getTextReviewTitle(), this.getRevisionSections());
  }

  public Sections getRevisionSections() {
    return new Sections(this.getTextReviewTitle(), this.projectId, getFacts());
  }

  public List<Contents> getFacts() {
    return new ArrayList<>();
  }

  public String getTextReviewTitle() {
    return this.reviewer + " 리뷰어가 " + this.reviewId + " 리뷰에 Accept 하였습니다.";
  }

}
