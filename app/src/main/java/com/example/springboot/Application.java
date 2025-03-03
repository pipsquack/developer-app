package com.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	final static String version = System.getenv().getOrDefault("DD_VERSION", "0.0.0");

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
