package com.ogcnice.footballteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot.
 *
 * L'application démarre sur le port 8080 par défaut.
 * Console H2 disponible sur: http://localhost:8080/h2-console
 */
@SpringBootApplication
public class FootballTeamApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballTeamApiApplication.class, args);
	}

}
