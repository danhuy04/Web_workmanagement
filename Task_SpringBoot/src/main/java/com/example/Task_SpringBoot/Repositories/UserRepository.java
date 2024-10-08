package com.example.Task_SpringBoot.Repositories;

import com.example.Task_SpringBoot.entities.User;
import com.example.Task_SpringBoot.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    static Optional<User> findByUserRole(UserRole userRole) {
    }

    Optional<User> findFirstByEmail(String username);

}
