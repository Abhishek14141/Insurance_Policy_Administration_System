package org.example.insurance_policy_system.repository.auth;

import java.util.Optional;

import org.example.insurance_policy_system.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
