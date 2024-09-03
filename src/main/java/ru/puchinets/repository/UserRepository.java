package ru.puchinets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.puchinets.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
