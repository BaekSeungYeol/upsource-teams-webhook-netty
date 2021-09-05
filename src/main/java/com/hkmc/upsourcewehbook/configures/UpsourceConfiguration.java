package com.hkmc.upsourcewehbook.configures;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(prefix = "upsource")
@Getter
@Setter
@ToString
public class UpsourceConfiguration {

  String apiURL;

  String basicHeader;

  String testURL;

  String teamsURL;

}
