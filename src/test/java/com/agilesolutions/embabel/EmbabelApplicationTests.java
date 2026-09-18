package com.agilesolutions.embabel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.Disabled;

@Import(TestcontainersConfiguration.class)
@Disabled("Skipping full context startup in CI; focus on Postgres integration tests")
@SpringBootTest
class EmbabelApplicationTests {

	@Test
	void contextLoads() {
	}

}
