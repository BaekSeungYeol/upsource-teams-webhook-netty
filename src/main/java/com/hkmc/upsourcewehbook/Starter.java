package com.hkmc.upsourcewehbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hkmc.upsourcewehbook.configures.UpsourceConfiguration;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableConfigurationProperties(UpsourceConfiguration.class)
public class Starter {

  public static void main(String[] args) {
    BlockHound.install();

    SpringApplication.run(Starter.class, args);
  }

}
