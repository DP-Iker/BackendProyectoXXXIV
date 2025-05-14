package com.xxxiv;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Proyecto34Application {

	public static void main(String[] args) {
		// Carga las variables del archivo .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// Inyecta variables de entorno en System para que Spring las lea
		System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
		System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));

		SpringApplication.run(Proyecto34Application.class, args);
	}

}
