package com.example.rest_service_4;

import lombok.Data;

@Data
public class UserDto {
    Integer id;
    String email;
    String fullName;
    String password;

    public UserDto(){}

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        fullName = user.getFullName();
        password = user.getPassword();
    }
}
