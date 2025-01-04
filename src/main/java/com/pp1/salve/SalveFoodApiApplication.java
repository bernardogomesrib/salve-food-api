package com.pp1.salve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;

@SpringBootApplication(exclude = {SpringDataWebAutoConfiguration.class})
public class SalveFoodApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalveFoodApiApplication.class, args);
	}

}
