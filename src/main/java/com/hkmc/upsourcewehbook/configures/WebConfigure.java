package com.hkmc.upsourcewehbook.configures;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebConfigure {

  @Bean
  public WebClient webClient() throws SSLException {

    SslContext ssl = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

    HttpClient httpClient = HttpClient.create()
                              .secure(builder -> builder.sslContext(ssl))
                              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
                              .responseTimeout(Duration.ofMillis(15000))
                              .doOnConnected(conn ->
                                               conn.addHandlerLast(new ReadTimeoutHandler(15000, TimeUnit.MILLISECONDS))
                                                 .addHandlerLast(
                                                   new WriteTimeoutHandler(15000, TimeUnit.MILLISECONDS)));

    return WebClient.builder()
             .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
             .defaultHeader("Access-Control-Allow-Origin", "*")
             .clientConnector(new ReactorClientHttpConnector(httpClient))
             .build();
  }

}
