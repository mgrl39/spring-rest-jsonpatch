package com.github.mgrl39.springrestjsonpatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserDAO userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserbyId(Integer id) {
        Optional<User> op = userRepository.findById(id);
        if(op.isPresent()) return op.get();
        else return null;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
