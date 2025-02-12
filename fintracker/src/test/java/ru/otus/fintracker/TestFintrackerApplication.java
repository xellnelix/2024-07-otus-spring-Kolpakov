package ru.otus.fintracker;

import org.springframework.boot.SpringApplication;

public class TestFintrackerApplication {

	public static void main(String[] args) {
		SpringApplication.from(FintrackerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
