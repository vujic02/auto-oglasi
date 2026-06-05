package com.autooglasi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ulazna tacka aplikacije za oglase automobila.
 * Spring MVC + Thymeleaf + Spring Security + JPA (MySQL).
 */
@SpringBootApplication
public class AutoOglasiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoOglasiApplication.class, args);
    }
}
