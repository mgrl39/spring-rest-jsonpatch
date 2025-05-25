package com.github.mgrl39.springrestjsonpatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servei que implementa la lògica de negoci per a la gestió d'usuaris.
 * Actua com a capa intermèdia entre el controlador i el repositori de dades.
 * Proporciona operacions CRUD bàsiques per a l'entitat User.
 *
 * @author mgrl39
 * @version 1.0
 * @see UserDAO
 * @see User
 */
@Service
public class UserService {

    /** Repositori per accedir a les dades dels usuaris */
    @Autowired
    UserDAO userRepository;

    /**
     * Recupera tots els usuaris del sistema.
     *
     * @return Llista amb tots els usuaris emmagatzemats
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Cerca un usuari pel seu identificador.
     * Si l'usuari no existeix, retorna null.
     *
     * @param id Identificador únic de l'usuari a cercar
     * @return L'usuari trobat o null si no existeix
     */
    public User getUserbyId(Integer id) {
        Optional<User> op = userRepository.findById(id);
        if(op.isPresent()) return op.get();
        else return null;
    }

    /**
     * Afegeix o actualitza un usuari al sistema.
     * Si l'usuari té un ID existent, s'actualitza; si no, es crea un de nou.
     *
     * @param user Entitat User a desar
     * @return L'usuari desat amb les seves dades actualitzades
     */
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Elimina un usuari del sistema pel seu identificador.
     * Si l'usuari no existeix, no fa res.
     *
     * @param id Identificador únic de l'usuari a eliminar
     */
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
