package com.switchfully.spectangular.services;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.CreateUserDto;
import com.switchfully.spectangular.dtos.SessionDto;
import com.switchfully.spectangular.dtos.UserDto;
import com.switchfully.spectangular.mappers.SessionMapper;
import com.switchfully.spectangular.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserService userService;
    @Mock
    private SessionMapper sessionMapper;
    @InjectMocks
    private SessionService sessionService;

    private User coachee;
    private User coach;
    private Session session;
    private CreateSessionDto createSessionDto;
    private SessionDto sessionDto;
    private String token;

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

        createSessionDto = new CreateSessionDto();
        createSessionDto.setSubject(session.getSubject());
        createSessionDto.setDate(session.getDate().toString());
        createSessionDto.setStartTime(session.getStartTime().toString());
        createSessionDto.setLocation(session.getLocation());
        createSessionDto.setRemarks(session.getRemarks());
        createSessionDto.setCoacheeId(session.getCoachee().getId());
        createSessionDto.setCoachId(session.getCoach().getId());

        sessionDto = new SessionDto();
        sessionDto.setId(session.getId());
        sessionDto.setSubject(session.getSubject());
        sessionDto.setDate(session.getDate().toString());
        sessionDto.setStartTime(session.getStartTime().toString());
        sessionDto.setLocation(session.getLocation());
        sessionDto.setRemarks(session.getRemarks());
        sessionDto.setCoachee_id(session.getCoachee().getId());
        sessionDto.setCoach_id(session.getCoach().getId());
    }

    //TODO: how to pass on an encoded test token
    @Test
    void createSession_givenCreateSessionDtoAndToken_thenReturnSessionDto() {
        //GIVEN
        Mockito.when(userService.findUserById(1)).thenReturn(coachee);
        Mockito.when(userService.findUserById(2)).thenReturn(coach);
        Mockito.when(sessionRepository.save(any())).thenReturn(session);
        Mockito.when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        //WHEN
        //THEN
    }

}
