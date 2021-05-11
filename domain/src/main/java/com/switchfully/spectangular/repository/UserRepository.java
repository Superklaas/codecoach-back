package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);
}
