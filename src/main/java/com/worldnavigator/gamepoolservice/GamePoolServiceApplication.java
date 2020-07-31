package com.worldnavigator.gamepoolservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class GamePoolServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GamePoolServiceApplication.class, args);
  }
}
