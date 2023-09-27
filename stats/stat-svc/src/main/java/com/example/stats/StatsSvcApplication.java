package com.example.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.example.stats.repository")
@EntityScan(basePackages = "package com.example.stats.model")
@SpringBootApplication()
public class StatsSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsSvcApplication.class, args);
    }

}
