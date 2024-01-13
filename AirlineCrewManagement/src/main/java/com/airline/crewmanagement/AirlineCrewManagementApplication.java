package com.airline.crewmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirlineCrewManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineCrewManagementApplication.class, args);
	}

}
