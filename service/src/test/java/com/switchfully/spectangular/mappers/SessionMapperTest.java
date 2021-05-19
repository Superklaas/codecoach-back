package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SessionMapperTest {
    private final SessionMapper sessionMapper = new SessionMapper();


    private User coachee;
    private User coach;
    private Session session;
    private CreateSessionDto createSessionDto;
    private SessionDto sessionDto;

    @BeforeEach
    private void setUp() {
        coachee = new User(
                "Test",
                "McTestFace",
                "TestFace",
                "test_mctestface@email.com",
                "Passw0rd",
                Role.COACHEE
        );
        coach = new User(
                "Coach",
                "McCoachFace",
                "CoachFace",
                "coach_mctestface@email.com",
                "Passw0rd",
                Role.COACH
        );
        session = new Session(
                "Spring",
                LocalDate.now().plusDays(1),
                LocalTime.now(),
                "Zoom",
                "These are remarks.",
                coach,
                coachee);

        createSessionDto = new CreateSessionDto()
                .setSubject(session.getSubject())
                .setDate(session.getDate().toString())
                .setStartTime(session.getStartTime().toString())
                .setLocation(session.getLocation())
                .setRemarks(session.getRemarks())
                .setCoacheeId(2)
                .setCoachId(1);

        sessionDto = new SessionDto()
                .setId(1)
                .setSubject(session.getSubject())
                .setDate(session.getDate().toString())
                .setLocation(session.getLocation())
                .setRemarks(session.getRemarks())
                .setCoacheeId(2)
                .setCoachId(1);
    }

    //TODO: figure out how to map id's -> NullPointer thrown in toDto

}
