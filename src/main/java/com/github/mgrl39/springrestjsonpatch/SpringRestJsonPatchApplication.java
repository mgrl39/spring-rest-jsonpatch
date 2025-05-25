package com.github.mgrl39.springrestjsonpatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal de l'aplicació Spring Boot que gestiona operacions REST amb suport per a JSON Patch.
 * Aquesta aplicació demostra les operacions CRUD bàsiques i la integració de JSON Patch per a
 * actualitzacions parcials d'entitats.
 * 
 * @author mgrl39
 * @version 1.0
 */
@SpringBootApplication
public class SpringRestJsonPatchApplication {

	/**
	 * Punt d'entrada principal de l'aplicació.
	 * Inicialitza el context de Spring Boot i posa en marxa l'aplicació web.
	 *
	 * @param args Arguments de línia de comandes passats a l'aplicació
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringRestJsonPatchApplication.class, args);
	}

}
