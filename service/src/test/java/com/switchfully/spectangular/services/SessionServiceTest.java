package com.switchfully.spectangular.services;

import com.switchfully.spectangular.JSONObjectParser;
import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.domain.session.SessionStatus;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import com.switchfully.spectangular.exceptions.UnauthorizedException;
import com.switchfully.spectangular.mappers.SessionMapper;
import com.switchfully.spectangular.repository.SessionRepository;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Optional;

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
    private User coach2;
    private User coachee;
    private Session session;
    private CreateSessionDto createSessionDto;
    private SessionDto sessionDto;
    private JSONObject testJsonObject;
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

        coach2 = new User(
                "Coach2",
                "McCoachFace2",
                "CoachFace2",
                "coach_mctestface2@email.com",
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


        try {
            testJsonObject = new JSONObject("{\"sub\": \"4\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Test
    void getAllSessionByCoach_givenSessionWithDateInThePastAndStatusRequested_updatesStatusSessionToDeclined(){
        session.setDate(LocalDate.now().minusDays(5));

        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));

        sessionService.getAllSessionByCoach(TOKEN);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.DECLINED_AUTOMATICALLY);
    }

    @Test
    void getAllSessionByCoach_givenSessionWithDateInThePastAndStatusAccepted_updatesStatusSessionTo_WAITIN_FEEDBACK(){
        //WHEN
        session.setDate(LocalDate.now().minusDays(5));
        session.setStatus(SessionStatus.ACCEPTED);
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));
        //GIVEN
        sessionService.getAllSessionByCoach(TOKEN);
        //THEN
        assertThat(session.getStatus()).isEqualTo(SessionStatus.WAITING_FEEDBACK);
    }

    @Test
    void getAllSessionByCoach_givenSessionWithDateInTheFutureAndStatusRequested_doesNotUpdateSession(){
        session.setDate(LocalDate.now().plusDays(5));

        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));

        sessionService.getAllSessionByCoach(TOKEN);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.REQUESTED);
    }

    @Test
    void getAllSessionByCoachee_givenSessionWithDateInThePastAndStatusRequested_updatesStatusSessionToDeclined(){

        //GIVEN
        session.setDate(LocalDate.now().minusDays(5));
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findAllByCoachee(coachee)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));

        //WHEN
        sessionService.getAllSessionByCoachee(TOKEN);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.DECLINED_AUTOMATICALLY);
    }

    @Test
    void updateSessionStatus_withInvalidId_throwsIllegalArgumentException(){
        //WHEN
        when(sessionRepository.findById(5)).thenReturn(Optional.empty());

        //GIVEN

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sessionService.updateSessionStatus(5, SessionStatus.ACCEPTED));
    }

    @Test
    void updateSessionStatus_validIdAndValidStatus_callsSetStatusForSession(){
        //WHEN
        Session mockSession = mock(Session.class);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(mockSession));
        doNothing().when(mockSession).setStatus(SessionStatus.ACCEPTED);
        //GIVEN
        sessionService.updateSessionStatus(5, SessionStatus.ACCEPTED);

        verify(mockSession, times(1)).setStatus(SessionStatus.ACCEPTED);
    }

    @Test
    void userHasAuthorityToChangeState_CoachisCoachOfSessionIdAndValidStatusChange_returnTrue(){
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(TOKEN,5,SessionStatus.ACCEPTED)).isTrue();
    }

    @Test
    void userHasAuthorityToChangeState_CoachisCoachOfSessionIdAndInvalidStatusChange_returnFalse(){
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(TOKEN,5,SessionStatus.REQUEST_CANCELLED_BY_COACHEE)).isFalse();
    }

    @Test
    void userHasAuthorityToChangeState_CoachisNotCoachOfSessionIdAndvalidStatusChange_returnFalse(){
        when(userService.findUserById(anyInt())).thenReturn(coach2);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(TOKEN,5,SessionStatus.ACCEPTED)).isFalse();
    }

    @Test
    void userHasAuthorityToChangeState_CoacheeisCoacheeOfSessionIdAndvalidStatusChange_returnTrue(){
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(TOKEN,5,SessionStatus.REQUEST_CANCELLED_BY_COACHEE)).isTrue();
    }

    @Test
    void userHasAuthorityToChangeState_CoacheeisCoacheeOfSessionIdAndInvalidStatusChange_returnFalse(){
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(TOKEN,5,SessionStatus.ACCEPTED)).isFalse();
    }


}
