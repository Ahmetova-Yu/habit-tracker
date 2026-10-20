package com.habittracker.habit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.habittracker")
@EnableScheduling
public class HabitServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HabitServiceApplication.class, args);
    }
}