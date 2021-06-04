package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findUsersByRole(Role role);

    Optional<User> findByResetToken(String token);

    Optional<User> findDistinctFirstByProfileName(String profileName);
}
