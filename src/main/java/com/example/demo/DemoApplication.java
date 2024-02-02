package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
        System.out.println("Server running at http://localhost:8080/");
        System.out.println("Ensure you have set the TEMPORAL_CLOUD_API_KEY environment variable.");

    }

}
