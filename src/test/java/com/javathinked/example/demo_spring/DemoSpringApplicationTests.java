package com.javathinked.example.demo_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DemoSpringApplicationTests {

	@Test
	void contextLoads() {
		// Test que le contexte Spring se charge correctement
		// Ce test vérifie que toutes les configurations sont valides
	}

}
