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
    private final SessionMapper sessionMapper = new SessionMapper(new FeedbackMapper());

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
                LocalDate.of(2021,7,14),
                LocalTime.of(15,45),
                "These are remarks.",
                "Zoom",
                coach,
                coachee);

        createSessionDto = new CreateSessionDto()
                .setSubject("Spring")
                .setDate(LocalDate.of(2021,7,14).toString())
                .setStartTime(LocalTime.of(15,45).toString())
                .setLocation("Zoom")
                .setRemarks("These are remarks.")
                .setCoacheeId(2)
                .setCoachId(1);

        sessionDto = new SessionDto()
                .setSubject("Spring")
                .setDate(LocalDate.of(2021,7,14).toString())
                .setStartTime(LocalTime.of(15,45).toString())
                .setLocation("Zoom")
                .setRemarks("These are remarks.")
                .setCoacheeId(2)
                .setCoachId(1)
                .setCoachProfileName("CoachFace")
                .setCoacheeProfileName("TestFace")
                .setStatus("REQUESTED");
    }

    @Test
    void toEntity_givenCreateSessionDto_thenReturnSession() {
        //GIVEN
        //WHEN
        Session actualSession = sessionMapper.toEntity(createSessionDto, coach, coachee);
        //THEN
        assertThat(actualSession).isEqualTo(session);
    }

/* //cannot test entities because they aren't assigned with an id yet -> NullPointer

    @Test
    void toDto_givenSession_thenReturnSessionDto() {
        //GIVEN
        //WHEN
        SessionDto actualSessionDto = sessionMapper.toDto(session);
        //THEN
        assertThat(actualSessionDto).isEqualTo(sessionDto);
    }
*/

}
