package com.hkmc.upsourcewehbook.api.dto;

import org.springframework.boot.autoconfigure.cache.CacheProperties.Caffeine;
import org.springframework.cache.annotation.Cacheable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Contents {

  private String name;

  private String value;

  public Contents(String name, String value) {
    this.name = name;
    this.value = value;
  }

}
