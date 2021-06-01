package com.switchfully.spectangular.repository;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.domain.session.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findAllByCoach(User coach);

    List<Session> findAllByCoachee(User coachee);

    List<Session> findByStatusIn(Set<SessionStatus> statusSet);

}
