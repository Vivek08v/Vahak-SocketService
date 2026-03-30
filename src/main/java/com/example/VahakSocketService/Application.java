package com.example.VahakSocketService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan("com.example.vahakentityservice.models")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
        System.out.println("Hello, Server is Up!!");
	}

}
