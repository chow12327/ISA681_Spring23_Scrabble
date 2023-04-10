package com.isa681.scrabble;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EntityScan(basePackages = {"com.isa681.scrabble.entity"})
public class Isa681Spring23ScrabbleApplication {

	public static void main(String[] args) {
		SpringApplication.run(Isa681Spring23ScrabbleApplication.class, args);
	}

}
