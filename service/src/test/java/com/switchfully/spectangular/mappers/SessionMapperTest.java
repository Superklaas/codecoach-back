package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
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

    @BeforeEach
    public void setUp() {
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
                LocalDate.of(2045,7,14),
                LocalTime.of(15,45),
                "These are remarks.",
                "Zoom",
                coach,
                coachee);

        createSessionDto = new CreateSessionDto()
                .setSubject("Spring")
                .setDate(LocalDate.of(2045,7,14).toString())
                .setStartTime(LocalTime.of(15,45).toString())
                .setLocation("Zoom")
                .setRemarks("These are remarks.")
                .setCoacheeId(2)
                .setCoachId(1);

    }

    @Test
    void toEntity_givenCreateSessionDto_thenReturnSession() {
        //GIVEN
        //WHEN
        Session actualSession = sessionMapper.toEntity(createSessionDto, coach, coachee);
        //THEN
        assertThat(actualSession).isEqualTo(session);
    }
/* NULLPOINTEREXCEPTION

    @Test
    void toDto_givenSession_thenReturnSessionDto() {
        //GIVEN
        Session mockSession = mock(Session.class);
        User mockCoach = mock(User.class);
        User mockCoachee = mock(User.class);
        Mockito.when(mockCoach.getId()).thenReturn(1);
        Mockito.when(mockCoachee.getId()).thenReturn(2);
        Mockito.when(mockCoach.getProfileName()).thenReturn("CoachFace");
        Mockito.when(mockCoachee.getProfileName()).thenReturn("TestFace");
        Mockito.when(mockSession.getId()).thenReturn(1);
        Mockito.when(mockSession.getSubject()).thenReturn(session.getSubject());
        Mockito.when(mockSession.getStartTime()).thenReturn(session.getStartTime());
        Mockito.when(mockSession.getLocation()).thenReturn(session.getLocation());
        Mockito.when(mockSession.getRemarks()).thenReturn(session.getRemarks());
        Mockito.when(mockSession.getCoach()).thenReturn(mockCoach);
        Mockito.when(mockSession.getCoachee()).thenReturn(mockCoachee);
        Mockito.when(mockSession.getStatus()).thenReturn(session.getStatus());
        //WHEN
        SessionDto actualSessionDto = sessionMapper.toDto(mockSession);
        //THEN
        assertThat(actualSessionDto).isEqualTo(sessionDto);
    }
*/

}
