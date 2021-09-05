package com.hkmc.upsourcewehbook.api;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hkmc.upsourcewehbook.api.dto.ApiResult;
import com.hkmc.upsourcewehbook.models.impl.AcceptReview;
import com.hkmc.upsourcewehbook.models.impl.LabelReview;
import com.hkmc.upsourcewehbook.models.impl.RevisionReview;
import com.hkmc.upsourcewehbook.services.UpsourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.hkmc.upsourcewehbook.api.dto.ApiResult.failed;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UpsourceController {

  private final UpsourceService upsourceService;

  @PostMapping("/teams/label")
  public Mono<ApiResult<String>> sendLabelNotificationsToTeams(@RequestBody String json) {

    return Mono.just(json)
             .filter(LabelReview::checkLabelAdded)
             .flatMap(upsourceService::getTeamsDtoWhenLabelAddedNew)
             .flatMap(upsourceService::sendMessageToTeams)
             .log()
             .map(ApiResult::succeed).log();
  }

  @PostMapping("/teams/revision")
  public Mono<ApiResult<String>> sendRevisionNotificationsToTeams(@RequestBody String json) {

    return Mono.just(json)
             .filter(RevisionReview::checkRevisionAdded)
             .flatMap(upsourceService::getTeamsDtoWhenRevisionCreated)
             .flatMap(upsourceService::sendMessageToTeams)
             .log()
             .map(ApiResult::succeed).log();
  }

  @PostMapping("/teams/accept")
  public Mono<ApiResult<String>> sendAcceptMessageToTeams(@RequestBody String json) {

    return Mono.just(json)
             .filter(AcceptReview::checkAcceptAdded)
             .flatMap(upsourceService::getTeamsDtoWhenAcceptAdded)
             .flatMap(upsourceService::sendMessageToTeams)
             .log()
             .map(ApiResult::succeed).log();
  }

  @ExceptionHandler(RuntimeException.class)
  public ApiResult<?> handleRuntimeException(RuntimeException e) {
    return failed(
      e.getMessage()
    );
  }

}


