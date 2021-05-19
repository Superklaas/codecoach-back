package com.switchfully.spectangular.domain.session;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SessionTest {
    private User coachee;
    private User coach;

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
}
