package com.khamitov.tgproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.khamitov.tgproject"})
public class TgprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgprojectApplication.class, args);
	}
}
