package com.github.mgrl39.springrestjsonpatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {
    public static final String USERS = "/api/v0/users";

    @Autowired
    UserController userController;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userController.getUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        UserDto u = userController.getUserById(id);
        return ResponseEntity.ok().body(u);

    }

    @PostMapping
    public ResponseEntity<UserDto> newUser(@RequestBody UserDto user) {
        UserDto u = userController.newUser(user);
        return ResponseEntity.ok().body(u);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        userController.remove(id);
        return ResponseEntity.ok().body(null);
    }


}
