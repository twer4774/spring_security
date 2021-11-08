package com.walter.fastcampus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.walter.fastcampus.user",
        "com.walter.fastcampus"
})
@EntityScan(basePackages = {"com.walter.fastcampus.user.domain"})
@EnableJpaRepositories(basePackages = {"com.walter.fastcampus.user.repository"})
public class UserDetailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDetailsApplication.class, args);
    }
}
