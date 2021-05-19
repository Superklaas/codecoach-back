package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
}
