package com.pp1.salve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SalveFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalveFoodApplication.class, args);
	}

}
