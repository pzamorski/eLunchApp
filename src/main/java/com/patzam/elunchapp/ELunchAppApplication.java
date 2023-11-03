package com.patzam.elunchapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ELunchAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(ELunchAppApplication.class, args);
	}
}
