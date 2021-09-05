package com.hkmc.upsourcewehbook.study;

import java.time.Duration;

import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmc.upsourcewehbook.api.UpsourceControllerSliceTest;
import groovy.util.logging.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.jayway.jsonpath.JsonPath.parse;

@SpringBootTest
@Slf4j
public class BlockHoundTest {

  @Test
  @DisplayName("Thread.sleep 으로 인한 블로킹")
  void threadSleepIsABlockingCall() {
    Mono.delay(Duration.ofSeconds(1))
      .flatMap(tick -> {
        try {
          Thread.sleep(10);
          return Mono.just(true);
        } catch (InterruptedException e) {
          return Mono.error(e);
        }
      })
      .as(StepVerifier::create)
      .verifyErrorMatches(throwable -> {
        Assertions.assertThat(throwable.getMessage()).contains("Blocking call! java.lang.Thread.sleep");
        return true;
      });
  }

  @Test
  @DisplayName("코드 중간에 subscribe가 있는 블록킹.")
  void middleSubscribeBlockingTest() {
    Mono.delay(Duration.ofSeconds(1))
      .flatMap(tick -> blockingTest())
      .as(StepVerifier::create)
      .expectNext("TEST")
      .verifyComplete();
  }

  public void testSendMethod(String s) {

    try {
      Thread.sleep(3000L);
    }catch (Exception e) {
      e.printStackTrace();
    }

  }
  public Mono<String> blockingTest() {

    Mono.just("test")
      .map(String::toUpperCase).subscribe(this::testSendMethod);

    return Mono.just("test")
             .map(String::toUpperCase);
  }


  private String testJson() {
    return "{\n" +
             "  \"majorVersion\": 2020,\n" +
             "  \"minorVersion\": 1\n" +
             "}\n";
  }

  @Test
  @DisplayName("jayWay Test")
  void jayWay() {

    Mono.delay(Duration.ofSeconds(1))
      .map(it -> UpsourceControllerSliceTest.업소스로부터_오는_리뷰_완료_데이터_웹훅())
      .map(json -> parse(json).read("$.data.base.actor.userName", String.class))
      .as(StepVerifier::create)
      .consumeNextWith(s -> Assertions.assertThat(s).isNotNull())
      .verifyComplete();
  }

  @Test
  @DisplayName("기존 ObjectMapper")
  void objectMppaerTest() throws JsonProcessingException {

    Mono.delay(Duration.ofSeconds(1))
      .map(it -> testJson())
      .map(json -> {
        try {
          return new ObjectMapper().readValue(json, Version.class);
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
        return json;
      })
      .as(StepVerifier::create)
      .consumeNextWith(s -> Assertions.assertThat(s).isNotNull())
      .verifyComplete();

  }

  private static class Version {

    private String majorVersion;

    private String minorVersion;

    public Version() {
    }

    public String getMajorVersion() {
      return majorVersion;
    }

    public void setMajorVersion(String majorVersion) {
      this.majorVersion = majorVersion;
    }

    public String getMinorVersion() {
      return minorVersion;
    }

    public void setMinorVersion(String minorVersion) {
      this.minorVersion = minorVersion;
    }

  }


}
