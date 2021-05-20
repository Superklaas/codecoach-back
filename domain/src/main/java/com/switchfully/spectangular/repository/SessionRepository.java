package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findAllByCoach(User coach);

    List<Session> findAllByCoachee(User coachee);

}
