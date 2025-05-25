package com.github.mgrl39.springrestjsonpatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Controlador que gestiona la lògica de negoci relacionada amb els usuaris.
 * Actua com a capa intermèdia entre la capa de presentació (Resource) i la capa de servei.
 * S'encarrega de la conversió entre DTOs i entitats.
 *
 * @author mgrl39
 * @version 1.0
 * @see UserService
 * @see UserDto
 * @see User
 */
@Controller
public class UserController {

    /**
     * Servei que gestiona les operacions amb usuaris.
     * Injectat automàticament per Spring.
     */
    @Autowired
    UserService userService;

    /**
     * Obté tots els usuaris del sistema.
     * Converteix les entitats User a DTOs per a la seva presentació.
     *
     * @return Llista d'usuaris en format DTO
     */
    public List<UserDto> getUsers() {
        List<User> users = userService.getUsers();
        return users.stream().map(UserDto::new).toList();
    }

    /**
     * Obté un usuari específic pel seu identificador.
     * Converteix l'entitat User a DTO per a la seva presentació.
     *
     * @param id Identificador únic de l'usuari
     * @return DTO de l'usuari trobat
     */
    public UserDto getUserById(Integer id) {
        User user = userService.getUserbyId(id);
        return new UserDto(user);
    }

    /**
     * Crea un nou usuari al sistema.
     * Converteix el DTO rebut en una entitat User i després torna a convertir
     * el resultat en DTO per a la seva presentació.
     *
     * @param userDto DTO amb les dades del nou usuari
     * @return DTO de l'usuari creat amb les dades actualitzades
     */
    public UserDto newUser(UserDto userDto) {
        User user = new User(userDto);
        return new UserDto(userService.addUser(user));
    }

    /**
     * Elimina un usuari del sistema pel seu identificador.
     *
     * @param id Identificador únic de l'usuari a eliminar
     */
    public void remove(Integer id) {
        userService.deleteById(id);
    }
}
