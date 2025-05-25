package com.github.mgrl39.springrestjsonpatch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfície de persistència per a l'entitat User.
 * Proporciona operacions CRUD bàsiques heretades de JpaRepository
 * i mètodes específics per a la gestió d'usuaris.
 * 
 * @author mgrl39
 * @version 1.0
 * @see User
 * @see JpaRepository
 */
@Repository
public interface UserDAO extends JpaRepository<User,Integer> {
    
    /**
     * Recupera tots els usuaris de la base de dades.
     * 
     * @return Llista amb tots els usuaris existents
     */
    List<User> findAll();

    /**
     * Cerca un usuari pel seu identificador.
     * Utilitza Optional per gestionar el cas en què l'usuari no existeixi.
     * 
     * @param id Identificador únic de l'usuari a cercar
     * @return Optional que conté l'usuari si existeix
     */
    Optional<User> findById(@Param("id") Integer id);

    /**
     * Desa un usuari a la base de dades.
     * Si l'usuari ja existeix, l'actualitza; si no existeix, el crea.
     * 
     * @param user Entitat User a desar
     * @return L'usuari desat amb possibles modificacions (com l'ID generat)
     */
    User save(User user);
}
