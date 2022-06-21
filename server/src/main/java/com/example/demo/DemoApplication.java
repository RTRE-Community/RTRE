package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins="*")
public class DemoApplication {
	public static String BimPort = "http://host.docker.internal:8082";
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
