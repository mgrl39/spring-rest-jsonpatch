package com.example.rest_service_4;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private Integer id;
    private String email;
    @Column(name = "full_name")
    private String fullName;
    private String password;

    public User(){};

    public User(Integer id, String email, String fullName, String password) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public User(UserDto userDto) {
        id = userDto.getId();
        email = userDto.getEmail();
        fullName = userDto.getFullName();
        password = userDto.getPassword();
    }
}
