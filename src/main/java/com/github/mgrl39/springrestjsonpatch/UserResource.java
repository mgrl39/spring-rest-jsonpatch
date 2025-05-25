package com.github.mgrl39.springrestjsonpatch;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que exposa els endpoints per a la gestió d'usuaris.
 * Proporciona una API RESTful amb operacions CRUD bàsiques.
 * Tots els endpoints comencen amb la ruta base definida a {@link #USERS}.
 *
 * @author mgrl39
 * @version 1.0
 * @see UserController
 * @see UserDto
 */
@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    /** Ruta base per tots els endpoints relacionats amb usuaris */
    public static final String USERS = "/api/v0/users";

    /** Controlador que gestiona la lògica de negoci dels usuaris */
    @Autowired
    UserController userController;

    /**
     * Obté tots els usuaris del sistema.
     * GET /api/v0/users
     *
     * @return ResponseEntity amb la llista d'usuaris i codi HTTP 200 si tot és correcte
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userController.getUsers();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Obté un usuari específic pel seu identificador.
     * GET /api/v0/users/{id}
     *
     * @param id Identificador únic de l'usuari a recuperar
     * @return ResponseEntity amb l'usuari trobat i codi HTTP 200 si tot és correcte
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        UserDto u = userController.getUserById(id);
        return ResponseEntity.ok().body(u);
    }

    /**
     * Crea un nou usuari al sistema.
     * POST /api/v0/users
     *
     * @param user DTO amb les dades del nou usuari
     * @return ResponseEntity amb l'usuari creat i codi HTTP 200 si tot és correcte
     */
    @PostMapping
    public ResponseEntity<UserDto> newUser(@RequestBody UserDto user) {
        UserDto u = userController.newUser(user);
        return ResponseEntity.ok().body(u);
    }

    /**
     * Reemplaça completament un usuari existent.
     * PUT /api/v0/users/{id}
     *
     * @param id Identificador únic de l'usuari a reemplaçar
     * @param userDto Noves dades de l'usuari
     * @return ResponseEntity amb l'usuari actualitzat i codi HTTP 200 si tot és correcte
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> replaceUser(
            @PathVariable Integer id,
            @RequestBody UserDto userDto) {
        userDto.setId(id); // Assegurem que l'ID sigui el correcte
        UserDto updated = userController.updateUser(userDto);
        return ResponseEntity.ok().body(updated);
    }

    /**
     * Elimina un usuari del sistema.
     * DELETE /api/v0/users/{id}
     *
     * @param id Identificador únic de l'usuari a eliminar
     * @return ResponseEntity amb cos buit i codi HTTP 200 si tot és correcte
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        userController.remove(id);
        return ResponseEntity.ok().body(null);
    }

    /**
     * Actualitza parcialment un usuari utilitzant JSON Patch.
     * PATCH /api/v0/users/{id}
     * Exemple de patch:
     * [
     *   { "op": "replace", "path": "/email", "value": "nou@email.com" }
     * ]
     *
     * @param id Identificador únic de l'usuari a actualitzar
     * @param patch Document JSON Patch amb les operacions a aplicar
     * @return ResponseEntity amb l'usuari actualitzat i codi HTTP 200 si tot és correcte
     */
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UserDto> patchUser(
            @PathVariable Integer id,
            @RequestBody JsonPatch patch) {
        UserDto updated = userController.patchUser(id, patch);
        return ResponseEntity.ok().body(updated);
    }
}
