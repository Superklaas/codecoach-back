package com.switchfully.spectangular.domain.session;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionTest {
    private User coachee;
    private User coach;
    private Session session;

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
                LocalDate.now(),
                LocalTime.now().plusHours(2),
                "Zoom",
                "These are remarks.",
                coach,
                coachee);
    }
    @Test
    void session_givenInvalidDate_thenIllegalArgumentExceptionIsThrown() {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Session(
                "Spring",
                LocalDate.now().minusDays(2),
                LocalTime.now(),
                "Zoom",
                "These are remarks.",
                coach,
                coachee));
    }

    @Test
    void session_givenInvalidTime_thenIllegalArgumentExceptionIsThrown() {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Session(
                        "Spring",
                        LocalDate.now(),
                        LocalTime.now().minusHours(1),
                        "Zoom",
                        "These are remarks.",
                        coach,
                        coachee));
    }

    @Test
    void given_session_whenSetsValidStatus_SetStatus(){
        session.setStatus(SessionStatus.ACCEPTED);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.ACCEPTED);
    }

    @Test
    void given_session_whenSetsInvalidStatus_ThrowIllegalStateException(){
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> session.setStatus(SessionStatus.FEEDBACK_RECEIVED));
    }

    @Test
    void givenSession_WithStatusRequest_WithDateTimeInThePast_AutoUpdateChangesStatusTo_DECLINED_AUTOMATICALLY(){
        session.setDate(LocalDate.now().minusDays(1));
        session.autoUpdateSession();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.DECLINED_AUTOMATICALLY);
    }

    @Test
    void givenSession_WithStatusAccepted_WithDateTimeInThePast_AutoUpdateChangesStatusTo_WAITING_FEEDBACK(){
        session.setStatus(SessionStatus.ACCEPTED);
        session.setDate(LocalDate.now().minusDays(1));
        session.autoUpdateSession();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.WAITING_FEEDBACK);
    }

    @Test
    void givenSession_WithStatusRequest_WithDateTimeInTheFuture_AutoUpdateDoesNotChangeStatus(){
        session.setDate(LocalDate.now().plusDays(1));
        session.autoUpdateSession();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.REQUESTED);
    }

    @Test
    void givenSession_WithStatusWaitingForFeedBack_WithDateTimeInThePast_AutoUpdateDoesNotChangeStatus(){
        session.setStatus(SessionStatus.ACCEPTED);
        session.setStatus(SessionStatus.WAITING_FEEDBACK);
        session.setDate(LocalDate.now().minusDays(1));
        session.autoUpdateSession();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.WAITING_FEEDBACK);
    }



}
