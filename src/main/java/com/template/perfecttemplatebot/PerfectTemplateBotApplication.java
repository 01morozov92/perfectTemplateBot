package com.template.perfecttemplatebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PerfectTemplateBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerfectTemplateBotApplication.class, args);
    }

}
