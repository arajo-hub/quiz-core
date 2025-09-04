package com.judy.quizcore.quizcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuizcoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizcoreApplication.class, args);
	}

}
