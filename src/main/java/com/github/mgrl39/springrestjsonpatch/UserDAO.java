package com.github.mgrl39.springrestjsonpatch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User,Integer> {
    List<User> findAll();
    Optional<User> findById(@Param("id") Integer id);
    User save(User user);
}
