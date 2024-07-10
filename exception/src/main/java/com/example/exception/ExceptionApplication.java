package com.example.exception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
public class ExceptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExceptionApplication.class, args);
	}
}
