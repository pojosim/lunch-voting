package com.example.lunchvoting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.lunchvoting.*")
public class ApplicationConfiguration {

}
