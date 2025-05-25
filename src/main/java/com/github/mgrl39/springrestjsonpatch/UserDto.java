package com.github.mgrl39.springrestjsonpatch;

import lombok.Data;

/**
 * Objecte de Transferència de Dades (DTO) per a la gestió d'usuaris.
 * Aquesta classe s'utilitza per transferir dades d'usuari entre capes
 * sense exposar directament l'entitat de persistència.
 * Utilitza Lombok (@Data) per generar automàticament getters, setters i altres mètodes útils.
 *
 * @author mgrl39
 * @version 1.0
 * @see User
 */
@Data
public class UserDto {
    
    /** Identificador únic de l'usuari */
    Integer id;
    
    /** Adreça de correu electrònic de l'usuari */
    String email;
    
    /** Nom complet de l'usuari */
    String fullName;
    
    /** Contrasenya de l'usuari */
    String password;

    /**
     * Constructor per defecte.
     * Necessari per a la deserialització JSON.
     */
    public UserDto(){}

    /**
     * Constructor que crea un DTO a partir d'una entitat User.
     * Copia totes les propietats de l'entitat al DTO.
     *
     * @param user Entitat User de la qual es copien les dades
     */
    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        fullName = user.getFullName();
        password = user.getPassword();
    }
}
