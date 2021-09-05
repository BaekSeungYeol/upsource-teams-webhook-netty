package com.hkmc.upsourcewehbook.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.hkmc.upsourcewehbook.api.dto.Branch;
import com.hkmc.upsourcewehbook.api.dto.ReviewDetails;
import com.hkmc.upsourcewehbook.api.dto.TeamsHookDto;
import com.hkmc.upsourcewehbook.api.dto.UserInfo;
import com.hkmc.upsourcewehbook.configures.UpsourceConfiguration;
import com.hkmc.upsourcewehbook.exceptions.NotFoundUpsourceNameException;
import com.hkmc.upsourcewehbook.models.impl.AcceptReview;
import com.hkmc.upsourcewehbook.models.impl.LabelReview;
import com.hkmc.upsourcewehbook.models.impl.RevisionReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.jayway.jsonpath.JsonPath.parse;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpsourceService {

  private final WebClient webClient;

  private final UpsourceConfiguration upsourceConfiguration;

  public Mono<TeamsHookDto> getTeamsDtoWhenLabelAddedNew(String json) {

    Mono<LabelReview> labelReviewImpl = Mono.just(new LabelReview(json)).cache();
    Mono<String> names = labelReviewImpl
                           .map(labelReview -> getIds(labelReview.getProjectId(), labelReview.getReviewId()))
                           .flatMap(this::getUserNameFromUpsource);

    return Mono.zip(labelReviewImpl, names).map(tu -> tu.getT1().getRevisionTeamsDto(tu.getT2()));
  }

  public Mono<TeamsHookDto> getTeamsDtoWhenRevisionCreated(String json) {
    Mono<RevisionReview> revisionReviewImpl = Mono.just(new RevisionReview(json)).cache();
    Mono<String> branchInfo = revisionReviewImpl.flatMap(
      rs -> getReviewIdFromPullRequestBranch(rs.getProjectId(), rs.findQuery(json)));

    Mono<String> names = Mono.zip(revisionReviewImpl, branchInfo)
                           .map(tu -> tu.getT1().reviewAdded(tu.getT2()))
                           .map(revisionReview -> getIds(revisionReview.getProjectId(),
                                                         revisionReview.getReviewId()))
                           .flatMap(this::getUserNameFromUpsource);

    return Mono.zip(revisionReviewImpl, names).map(tu -> tu.getT1().getRevisionTeamsDto(tu.getT2()));
  }

  public Mono<TeamsHookDto> getTeamsDtoWhenAcceptAdded(String json) {
    return Mono.just(new AcceptReview(json)).map(AcceptReview::getRevisionTeamsDto);
  }

  public Mono<ArrayList<String>> getIds(String projectId, String reviewId) {
    return getParticipantsFromUpsource(projectId, reviewId).flatMap(this::parseToUserId);
  }

  public Mono<String> getUserNameFromUpsource(Mono<ArrayList<String>> ids) {
    return ids.flatMap(it -> getUserInfos(it).flatMap(this::parseToNameList));
  }

  public Mono<ArrayList<String>> parseToUserId(String json) {
    ArrayList<String> ids = new ArrayList<>();
    @SuppressWarnings("unchecked")
    ArrayList<LinkedHashMap<String, String>> participants = parse(json).read("$.result.participants", ArrayList.class);
    participants.stream().map(part -> part.get("userId")).forEach(ids::add);
    return Mono.just(ids);
  }

  public Mono<String> parseToNameList(String json) {
    @SuppressWarnings("unchecked")
    ArrayList<LinkedHashMap<String, String>> infos = parse(json).read("$.result.infos", ArrayList.class);
    String names = infos.stream().map(infoMap -> infoMap.get("name"))
                     .reduce((nameOne, nameTwo) -> nameOne + ", " + nameTwo)
                     .orElseThrow(NotFoundUpsourceNameException::new);
    return Mono.just(names);
  }

  private Mono<String> getUserInfos(ArrayList<String> it) {

    return webClient.post()
             .uri(upsourceConfiguration.getApiURL() + "/getUserInfo")
             .header("Authorization", upsourceConfiguration.getBasicHeader())
             .contentType(MediaType.APPLICATION_JSON)
             .body(BodyInserters.fromValue(new UserInfo(it)))
             .retrieve()
             .bodyToMono(String.class);
  }

  public Mono<String> getReviewIdFromPullRequestBranch(String projectId, String query) {
    return webClient.post()
             .uri(upsourceConfiguration.getApiURL() + "/getReviews")
             .header("Authorization", upsourceConfiguration.getBasicHeader())
             .contentType(MediaType.APPLICATION_JSON)
             .body(BodyInserters.fromValue(new Branch(projectId, query, 1)))
             .retrieve()
             .bodyToMono(String.class);
  }

  public Mono<String> getParticipantsFromUpsource(String projectId, String reviewId) {

    return webClient.post()
             .uri(upsourceConfiguration.getApiURL() + "/getReviewDetails")
             .header("Authorization", upsourceConfiguration.getBasicHeader())
             .contentType(MediaType.APPLICATION_JSON)
             .body(BodyInserters.fromValue(new ReviewDetails(projectId, reviewId)))
             .retrieve()
             .bodyToMono(String.class);
  }

  public Mono<String> sendMessageToTeams(TeamsHookDto teamsDto) {
    return webClient.post()
//             .uri(upsourceConfiguration.getTeamsURL())
             .uri(upsourceConfiguration.getTestURL())
             .contentType(MediaType.APPLICATION_JSON)
             .body(BodyInserters.fromValue(teamsDto))
             .retrieve()
             .bodyToMono(String.class).log();
  }

}
