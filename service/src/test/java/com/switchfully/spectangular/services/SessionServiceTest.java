package com.switchfully.spectangular.services;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import com.switchfully.spectangular.exceptions.UnauthorizedException;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private User coach;
    private User coachee;
    private Session session;
    private CreateSessionDto createSessionDto;
    private SessionDto sessionDto;
    //token with id/sub = 1234567980
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private static final String INVALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.falseWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private static final int COACH_ID = 2;
    private static final int COACHEE_ID = 1234567890;

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
                .setCoacheeId(COACHEE_ID)
                .setCoachId(COACH_ID);

        sessionDto = new SessionDto()
                .setSubject(session.getSubject())
                .setDate(session.getDate().toString())
                .setStartTime(session.getStartTime().toString())
                .setLocation(session.getLocation())
                .setRemarks(session.getRemarks())
                .setCoacheeId(COACHEE_ID)
                .setCoachId(COACH_ID);
    }

    @Test
    void createSession_givenCreateSessionDtoAndToken_thenReturnSessionDto() {
        //GIVEN
        Mockito.when(userService.findUserById(COACH_ID)).thenReturn(coach);
        Mockito.when(userService.findUserById(COACHEE_ID)).thenReturn(coachee);
        Mockito.when(sessionRepository.save(any())).thenReturn(session);
        Mockito.when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        //WHEN
        SessionDto actualSessionDto = sessionService.createSession(createSessionDto, TOKEN);
        //THEN
        verify(userService, times(2)).findUserById(COACH_ID);
        verify(userService).findUserById(COACHEE_ID);
        verify(sessionRepository).save(any());
        verify(sessionMapper).toDto(any());
        assertThat(actualSessionDto).isEqualTo(sessionDto);
    }

    @Test
    void createSession_givenNonMatchingCoacheeId_thenThrowUnauthorizedException() {
        //GIVEN
        CreateSessionDto invalidDto = new CreateSessionDto()
                .setSubject(session.getSubject())
                .setDate(session.getDate().toString())
                .setStartTime(session.getStartTime().toString())
                .setLocation(session.getLocation())
                .setRemarks(session.getRemarks())
                .setCoacheeId(1)
                .setCoachId(COACH_ID);
        //WHEN
        //THEN
        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> sessionService.createSession(invalidDto, TOKEN));
    }

    @Test
    void createSession_givenUserWhoIsNotCoach_thenThrowIllegalArgumentException() {
        //GIVEN
        User notCoach = new User(
                "NotACoach",
                "McNotCoachFace",
                "DefinitelyNotACoach",
                "nocoacheshere@email.com",
                "Passw0rd",
                Role.COACHEE
        );
        Mockito.when(userService.findUserById(1)).thenReturn(notCoach);
        CreateSessionDto invalidDto = new CreateSessionDto()
                .setSubject(session.getSubject())
                .setDate(session.getDate().toString())
                .setStartTime(session.getStartTime().toString())
                .setLocation(session.getLocation())
                .setRemarks(session.getRemarks())
                .setCoacheeId(COACHEE_ID)
                .setCoachId(1);
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sessionService.createSession(invalidDto, TOKEN));
    }

    @Test
    void getAllSessionByCoachee_givenValidTokenCoachee_thenReturnListSessionDtos() {
        //GIVEN
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findAllByCoachee(coachee)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));
        //WHEN
        List<SessionDto> actualSessionDtos = sessionService.getAllSessionByCoachee(TOKEN);
        //THEN
        assertThat(actualSessionDtos).containsExactly(sessionDto);
    }

    @Test
    void getAllSessionByCoach_givenValidTokenCoach_thenReturnListSessionDtos() {
        //GIVEN
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));
        //WHEN
        List<SessionDto> actualSessionDtos = sessionService.getAllSessionByCoach(TOKEN);
        //THEN
        assertThat(actualSessionDtos).containsExactly(sessionDto);
    }

    @Test
    void getAllSessionByCoach_givenUserWhoIsNotCoach_thenThrowIllegalArgumentException() {
        //GIVEN
        User notCoach = new User(
                "NotACoach",
                "McNotCoachFace",
                "DefinitelyNotACoach",
                "nocoacheshere@email.com",
                "Passw0rd",
                Role.COACHEE
        );
        when(userService.findUserById(anyInt())).thenReturn(notCoach);
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sessionService.getAllSessionByCoach(TOKEN));

    }


}
