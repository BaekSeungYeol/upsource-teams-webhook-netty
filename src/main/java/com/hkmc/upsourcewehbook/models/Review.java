package com.hkmc.upsourcewehbook.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface Review {

  String UPSOURCE_URL = "https://upsource.connected-car.io/";

  default String getFormattedDate() {
    return LocalDateTime.now().plusHours(9).atZone(ZoneId.of("Asia/Seoul")).format(
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  default String combineTargetURL(String projectId, String reviewId) {
    StringBuilder sb = new StringBuilder();
    sb.append(UPSOURCE_URL);
    sb.append(projectId);
    sb.append("/review/");
    sb.append(reviewId);
    return sb.toString();
  }

}
