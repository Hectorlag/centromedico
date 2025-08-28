package com.centromedico.sistema_turnos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SistemaTurnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaTurnosApplication.class, args);
	}

}
