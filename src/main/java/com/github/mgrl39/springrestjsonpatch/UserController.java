package com.github.mgrl39.springrestjsonpatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    public List<UserDto> getUsers() {
        List<User> users = userService.getUsers();
        return users.stream().map(UserDto::new).toList();
    }

    public UserDto getUserById(Integer id) {
        User user = userService.getUserbyId(id);
        return new UserDto(user);
    }

    public UserDto newUser(UserDto userDto) {
        User user = new User(userDto);
        return new UserDto(userService.addUser(user));
    }

    public void remove(Integer id) {
        userService.deleteById(id);
    }
}
