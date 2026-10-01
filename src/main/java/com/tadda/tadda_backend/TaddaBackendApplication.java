package com.tadda.tadda_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class TaddaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaddaBackendApplication.class, args);
	}

}
