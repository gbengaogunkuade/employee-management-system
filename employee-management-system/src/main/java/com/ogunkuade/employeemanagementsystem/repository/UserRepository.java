package com.ogunkuade.employeemanagementsystem.repository;


import com.ogunkuade.employeemanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findUserById(Long id);
    List<User> findByLastnameContainsIgnoreCase(String lastName);
    Boolean existsByUsername(String username);
    List<User> findUsersByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String word1, String word2);

}
