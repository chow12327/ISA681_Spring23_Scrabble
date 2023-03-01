package com.isa681.scrabble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Isa681Spring23ScrabbleApplication {

	public static void main(String[] args) {
		SpringApplication.run(Isa681Spring23ScrabbleApplication.class, args);
	}

}
