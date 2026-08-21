package com.agilesolutions.embabel;

import org.springframework.boot.SpringApplication;

public class TestEmbabelApplication {

	public static void main(String[] args) {
		SpringApplication.from(EmbabelApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
