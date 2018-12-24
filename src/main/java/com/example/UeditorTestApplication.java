package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UeditorTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(UeditorTestApplication.class, args);
	}

	@Bean
	public BinaryUploader getBinaryUploader(){
		return new BinaryUploader();
	}
}
