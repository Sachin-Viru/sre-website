package com.example.sre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class SreWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SreWebsiteApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "sre";
    }
}
