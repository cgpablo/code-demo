package com.cgpablo.prices.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EntityScan("com.cgpablo.prices.infrastructure.entity")
@SpringBootApplication(scanBasePackages = "com.cgpablo.prices")
@EnableJpaRepositories("com.cgpablo.prices.infrastructure.repository")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
