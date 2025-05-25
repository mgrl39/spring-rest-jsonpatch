package com.github.mgrl39.springrestjsonpatch;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entitat que representa un usuari al sistema.
 * Aquesta classe està mapejada a la taula 'users' de la base de dades
 * i utilitza JPA per a la persistència.
 *
 * @author mgrl39
 * @version 1.0
 */
@Data
@Entity
@Table(name = "users")
public class User {
    
    /** Identificador únic de l'usuari */
    @Id
    private Integer id;
    
    /** Adreça de correu electrònic de l'usuari */
    private String email;
    
    /** Nom complet de l'usuari */
    @Column(name = "full_name")
    private String fullName;
    
    /** Contrasenya de l'usuari */
    private String password;

    /**
     * Constructor per defecte necessari per JPA.
     */
    public User(){};

    /**
     * Constructor que crea un usuari amb totes les seves dades.
     *
     * @param id Identificador únic de l'usuari
     * @param email Adreça de correu electrònic
     * @param fullName Nom complet
     * @param password Contrasenya
     */
    public User(Integer id, String email, String fullName, String password) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    /**
     * Constructor que crea un usuari a partir d'un DTO.
     * Converteix un objecte UserDto en una entitat User.
     *
     * @param userDto Objecte DTO que conté les dades de l'usuari
     */
    public User(UserDto userDto) {
        id = userDto.getId();
        email = userDto.getEmail();
        fullName = userDto.getFullName();
        password = userDto.getPassword();
    }
}
