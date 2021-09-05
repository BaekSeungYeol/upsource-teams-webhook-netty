package com.hkmc.upsourcewehbook.api;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.hkmc.upsourcewehbook.api.dto.Sections;
import com.hkmc.upsourcewehbook.api.dto.TeamsHookDto;
import com.hkmc.upsourcewehbook.services.UpsourceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(UpsourceController.class)
@ExtendWith(MockitoExtension.class)
public class UpsourceControllerSliceTest {

  @Autowired
  WebTestClient client;

  @MockBean
  UpsourceService upsourceService;

  String label_json;

  String revision_json;

  String accept_json;

  String expectedBody;

  TeamsHookDto teamsHookDto;

  @BeforeEach
  void setUp() {
    label_json = 업소스로부터_오는_리뷰_생성_데이터_웹훅();
    revision_json = 업소스로부터_오는_리비전_데이터_웹훅();
    accept_json = 업소스로부터_오는_리뷰_완료_데이터_웹훅();

    expectedBody = "{\"success\":true,\"response\":\"1\",\"error\":null}";
  }

  @Test
  @DisplayName("Ready for Review 라벨을 눌러 리뷰 생성에 대한 요청이 성공한다")
  void sendLabelNotificationsToTeams() {

    teamsHookDto = new TeamsHookDto("리뷰가 생성되었습니다.", new Sections("리뷰가 생성되었습니다.", "ccsp20-test", new ArrayList<>()));
    when(upsourceService.getTeamsDtoWhenLabelAddedNew(anyString())).thenReturn(Mono.just(teamsHookDto));
    when(upsourceService.sendMessageToTeams(any(TeamsHookDto.class))).thenReturn(Mono.just("1"));

    client.post()
      .uri("/teams/label")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(label_json)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
      .expectBody(String.class)
      .consumeWith(exchangeResult -> {
        String responseBody = exchangeResult.getResponseBody();
        Assertions.assertThat(responseBody).isEqualTo(expectedBody);
      });
  }

  @Test
  @DisplayName(" Commit 내용이 추가로 Push 되어 Revision 요청이 성공한다")
  void sendRevisionNotificationsToTeams() {

    teamsHookDto = new TeamsHookDto("리뷰에 새 코드가 반영되었습니다.", new Sections("리뷰에 새 코드가 반영되었습니다.", "ccsp20-test",
                                                                       new ArrayList<>()));
    when(upsourceService.getTeamsDtoWhenRevisionCreated(anyString())).thenReturn(Mono.just(teamsHookDto));
    when(upsourceService.sendMessageToTeams(any(TeamsHookDto.class))).thenReturn(Mono.just("1"));

    client.post()
      .uri("/teams/revision")
      .bodyValue(revision_json)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
      .expectBody(String.class)
      .consumeWith(exchangeResult -> {
        String responseBody = exchangeResult.getResponseBody();
        Assertions.assertThat(responseBody).isEqualTo(expectedBody);
      });
  }

  @Test
  @DisplayName(" Accpet 를 눌렀을때 리뷰 완료에 대한 요청이 성공한다")
  void sendReviewNotificationsToTeams() {

    teamsHookDto = new TeamsHookDto("admin 리뷰어가 CB-CR-60 리뷰에 Accept 하였습니다.",
                                    new Sections("admin 리뷰어가 CB-CR-60 리뷰에 Accept 하였습니다.", "ccsp20-test",
                                                 new ArrayList<>()));
    when(upsourceService.getTeamsDtoWhenAcceptAdded(anyString())).thenReturn(Mono.just(teamsHookDto));
    when(upsourceService.sendMessageToTeams(any(TeamsHookDto.class))).thenReturn(Mono.just("1"));

    client.post()
      .uri("/teams/accept")
      .bodyValue(accept_json)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
      .expectBody(String.class)
      .consumeWith(exchangeResult -> {
        String responseBody = exchangeResult.getResponseBody();
        Assertions.assertThat(responseBody).isEqualTo(expectedBody);
      });
  }

  private String 업소스로부터_오는_리뷰_생성_데이터_웹훅() {
    return "{\n" +
             "  \"majorVersion\": 2020,\n" +
             "  \"minorVersion\": 1,\n" +
             "  \"projectId\": \"your-project\",\n" +
             "  \"dataType\": \"ReviewLabelChangedEventBean\",\n" +
             "  \"data\": {\n" +
             "    \"reviewId\": \"CB-CR-36\",\n" +
             "    \"labelId\": \"ready\",\n" +
             "    \"labelName\": \"ready for review\",\n" +
             "    \"wasAdded\": true,\n" +
             "    \"actor\": {\n" +
             "      \"userId\": \"51a6d3ff-ab69-4953-90bc-5a29df222745\",\n" +
             "      \"userName\": \"admin\"\n" +
             "    }\n" +
             "  }\n" +
             "}\n";
  }

  private String 업소스로부터_오는_리비전_데이터_웹훅() {
    return "{\n" +
             "  \"majorVersion\": 2020,\n" +
             "  \"minorVersion\": 1,\n" +
             "  \"projectId\": \"your-project\",\n" +
             "  \"dataType\": \"NewRevisionEventBean\",\n" +
             "  \"data\": {\n" +
             "    \"revisionId\": \"de3dfaba9225a79a5d31026dc9c4845c0c9ef91a\",\n" +
             "    \"branches\": [\n" +
             "      \"PR 19\",\n" +
             "      \"feature/DOE-241\",\n" +
             "      \"PR 19\",\n" +
             "      \"feature/DOE-241\"\n" +
             "    ],\n" +
             "    \"author\": \"백승열\",\n" +
             "    \"message\": \"[TEST 2] Feat : Test \",\n" +
             "    \"date\": 1626537436000\n" +
             "  }\n" +
             "}";
  }

  public static String 업소스로부터_오는_리뷰_완료_데이터_웹훅() {
    return "{\n" +
             "  \"majorVersion\": 2020,\n" +
             "  \"minorVersion\": 1,\n" +
             "  \"projectId\": \"your-project\",\n" +
             "  \"dataType\": \"ParticipantStateChangedFeedEventBean\",\n" +
             "  \"data\": {\n" +
             "    \"base\": {\n" +
             "      \"userIds\": [\n" +
             "        {\n" +
             "          \"userId\": \"9e3bc279-2221-488a-8e68-6b5dfff1face\",\n" +
             "          \"userName\": \"백승열\",\n" +
             "          \"userEmail\": \"seungyeol@seungyeol.com\"\n" +
             "        },\n" +
             "        {\n" +
             "          \"userId\": \"51a6d3ff-ab69-4953-90bc-5a29df222745\",\n" +
             "          \"userName\": \"admin\"\n" +
             "        }\n" +
             "      ],\n" +
             "      \"reviewNumber\": 60,\n" +
             "      \"reviewId\": \"CB-CR-60\",\n" +
             "      \"date\": 1626587313108,\n" +
             "      \"actor\": {\n" +
             "        \"userId\": \"51a6d3ff-ab69-4953-90bc-5a29df222745\",\n" +
             "        \"userName\": \"admin\"\n" +
             "      },\n" +
             "      \"feedEventId\": \"1626587313108#your-project#61ac5725-118d-4d00-baf4" +
             "-0faf2d079ce8\"\n" +
             "    },\n" +
             "    \"participant\": {\n" +
             "      \"userId\": \"51a6d3ff-ab69-4953-90bc-5a29df222745\",\n" +
             "      \"userName\": \"admin\"\n" +
             "    },\n" +
             "    \"oldState\": 1,\n" +
             "    \"newState\": 2\n" +
             "  }\n" +
             "}";
  }

}
