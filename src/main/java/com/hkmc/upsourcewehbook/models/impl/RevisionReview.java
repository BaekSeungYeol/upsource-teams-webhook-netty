package com.hkmc.upsourcewehbook.models.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hkmc.upsourcewehbook.api.dto.Action;
import com.hkmc.upsourcewehbook.api.dto.Contents;
import com.hkmc.upsourcewehbook.api.dto.PotentialAction;
import com.hkmc.upsourcewehbook.api.dto.Sections;
import com.hkmc.upsourcewehbook.api.dto.TargetURL;
import com.hkmc.upsourcewehbook.api.dto.TeamsHookDto;
import com.hkmc.upsourcewehbook.exceptions.NotFoundPullRequestException;
import com.hkmc.upsourcewehbook.models.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.jayway.jsonpath.JsonPath.parse;

@Getter
@Setter
@ToString
public class RevisionReview implements Review {

  private String reviewTitle;

  private String projectId;

  private String author;

  private String reviewId;

  // ReviewLabelChangedEventBean
  public RevisionReview(String json) {
    this.reviewTitle = "리뷰에 새 코드가 반영되었습니다.";
    this.projectId = parse(json).read("$.projectId", String.class);
    this.author = parse(json).read("$.data.author", String.class);
    this.reviewId = "";
  }

  public String findQuery(String json) {
    @SuppressWarnings("unchecked")
    ArrayList<String> branchNames = parse(json).read("$.data.branches", ArrayList.class);

    return branchNames.stream()
             .filter(branchName -> branchName.startsWith("PR"))
             .findFirst()
             .map(this::getPRNumberNoSpace)
             .map(this::getQuery)
             .orElseThrow(NotFoundPullRequestException::new);
  }

  public String getQuery(String PR) {
    return "branch: " + PR;
  }

  public static boolean checkRevisionAdded(String json) {
    return "NewRevisionEventBean".equals(parse(json).read("$.dataType", String.class));
  }

  public RevisionReview reviewAdded(String reviewJson) {
    String reviewId = parse(reviewJson).read("$.result.reviews[0].reviewId.reviewId", String.class);
    this.setReviewId(reviewId);
    return this;
  }

  private String getPRNumberNoSpace(String PR) {
    StringBuilder sb = new StringBuilder();
    String[] s = PR.split(" ");
    sb.append(s[0]);
    sb.append(s[1]);
    return sb.toString();
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
