package com.switchfully.spectangular.services;

import com.switchfully.spectangular.JSONObjectParser;
import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.FeedbackForCoach;
import com.switchfully.spectangular.domain.session.FeedbackForCoachee;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.domain.session.SessionStatus;
import com.switchfully.spectangular.dtos.*;
import com.switchfully.spectangular.exceptions.UnauthorizedException;
import com.switchfully.spectangular.mappers.FeedbackMapper;
import com.switchfully.spectangular.mappers.SessionMapper;
import com.switchfully.spectangular.repository.SessionRepository;
import com.switchfully.spectangular.services.mailing.EmailService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
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
    private EmailService emailService;
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private FeedbackMapper feedbackMapper;
    @InjectMocks
    private SessionService sessionService;

    private User coach;
    private User coach2;
    private User coachee;
    private Session session;
    private CreateSessionDto createSessionDto;
    private SessionDto sessionDto;
    private AddFeedbackForCoacheeDto addCoacheeFeedbackDto;
    private AddFeedbackForCoachDto addCoachFeedbackDto;
    private JSONObject testJsonObject;
    //token with id/sub = 1234567980
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
            ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private static final String INVALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".falseWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
            ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
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

        addCoacheeFeedbackDto = new AddFeedbackForCoacheeDto()
                .setPreparedness((short) 1)
                .setWillingness((short) 2)
                .setPositive("good good")
                .setNegative("none");

        addCoachFeedbackDto = new AddFeedbackForCoachDto()
                .setExplanation((short) 1)
                .setUsefulness((short) 2)
                .setPositive("good good")
                .setNegative("none");


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
        SessionDto actualSessionDto = sessionService.createSession(createSessionDto, COACHEE_ID);
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
                .setCoacheeId(COACHEE_ID)
                .setCoachId(COACH_ID);
        //THEN
        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> sessionService.createSession(invalidDto, 1));
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
                .isThrownBy(() -> sessionService.createSession(invalidDto, COACHEE_ID));
    }

    @Test
    void getAllSessionByCoachee_givenValidCoacheeId_thenReturnListSessionDtos() {
        //GIVEN
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findAllByCoachee(coachee)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));
        //WHEN
        List<SessionDto> actualSessionDtos = sessionService.getAllSessionByCoachee(COACHEE_ID);
        //THEN
        assertThat(actualSessionDtos).containsExactly(sessionDto);
    }

    @Test
    void getAllSessionByCoach_givenValidCoachId_thenReturnListSessionDtos() {
        //GIVEN
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));
        //WHEN
        List<SessionDto> actualSessionDtos = sessionService.getAllSessionByCoach(COACH_ID);
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
                .isThrownBy(() -> sessionService.getAllSessionByCoach(COACHEE_ID));

    }

    @Test
    void getAllSessionByCoach_givenSessionWithDateInThePastAndStatusRequested_updatesStatusSessionToDeclined() {
        session.setDate(LocalDate.now().minusDays(5));

        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));

        sessionService.getAllSessionByCoach(COACH_ID);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.DECLINED_AUTOMATICALLY);
    }

    @Test
    void getAllSessionByCoach_givenSessionWithDateInThePastAndStatusAccepted_updatesStatusSessionTo_WAITIN_FEEDBACK() {
        //WHEN
        session.setDate(LocalDate.now().minusDays(5));
        session.setStatus(SessionStatus.ACCEPTED);
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));
        //GIVEN
        sessionService.getAllSessionByCoach(COACH_ID);
        //THEN
        assertThat(session.getStatus()).isEqualTo(SessionStatus.WAITING_FEEDBACK);
    }

    @Test
    void getAllSessionByCoach_givenSessionWithDateInTheFutureAndStatusRequested_doesNotUpdateSession() {
        session.setDate(LocalDate.now().plusDays(5));

        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findAllByCoach(coach)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));

        sessionService.getAllSessionByCoach(COACH_ID);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.REQUESTED);
    }

    @Test
    void getAllSessionByCoachee_givenSessionWithDateInThePastAndStatusRequested_updatesStatusSessionToDeclined() {

        //GIVEN
        session.setDate(LocalDate.now().minusDays(5));
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findAllByCoachee(coachee)).thenReturn(List.of(session));
        when(sessionMapper.toListOfDtos(List.of(session))).thenReturn(List.of(sessionDto));

        //WHEN
        sessionService.getAllSessionByCoachee(COACHEE_ID);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.DECLINED_AUTOMATICALLY);
    }

    @Test
    void updateSessionStatus_withInvalidId_throwsIllegalArgumentException() {
        //WHEN
        when(sessionRepository.findById(5)).thenReturn(Optional.empty());

        //GIVEN

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sessionService.updateSessionStatus(COACH_ID, 5, SessionStatus.ACCEPTED));
    }

    @Test
    void updateSessionStatus_validIdAndValidStatus_callsSetStatusForSession() {
        //WHEN
        Session mockSession = mock(Session.class);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(mockSession));
        when(mockSession.getCoach()).thenReturn(coach);
        when(userService.findUserById(COACH_ID)).thenReturn(coach);
        doNothing().when(mockSession).setStatus(SessionStatus.ACCEPTED);
        //GIVEN
        sessionService.updateSessionStatus(COACH_ID, 5, SessionStatus.ACCEPTED);

        verify(mockSession, times(1)).setStatus(SessionStatus.ACCEPTED);
    }

    @Test
    void userHasAuthorityToChangeState_CoachisCoachOfSessionIdAndValidStatusChange_returnTrue() {
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(COACH_ID, 5, SessionStatus.ACCEPTED)).isTrue();
    }

    @Test
    void userHasAuthorityToChangeState_CoachisCoachOfSessionIdAndInvalidStatusChange_returnFalse() {
        when(userService.findUserById(anyInt())).thenReturn(coach);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(COACH_ID, 5,
                SessionStatus.REQUEST_CANCELLED_BY_COACHEE)).isFalse();
    }

    @Test
    void userHasAuthorityToChangeState_CoachisNotCoachOfSessionIdAndvalidStatusChange_returnFalse() {
        when(userService.findUserById(anyInt())).thenReturn(coach2);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(COACH_ID, 5, SessionStatus.ACCEPTED)).isFalse();
    }

    @Test
    void userHasAuthorityToChangeState_CoacheeisCoacheeOfSessionIdAndvalidStatusChange_returnTrue() {
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(COACHEE_ID, 5,
                SessionStatus.REQUEST_CANCELLED_BY_COACHEE)).isTrue();
    }

    @Test
    void userHasAuthorityToChangeState_CoacheeisCoacheeOfSessionIdAndInvalidStatusChange_returnFalse() {
        when(userService.findUserById(anyInt())).thenReturn(coachee);
        when(sessionRepository.findById(5)).thenReturn(Optional.of(session));

        assertThat(sessionService.userHasAuthorityToChangeState(COACHEE_ID, 5, SessionStatus.ACCEPTED)).isFalse();
    }

    @Test
    void addFeedbackForCoachee_givenSessionIdUserIdAndFeedbackDto_thenReturnSessionDto() {
        //GIVEN
        Session mockSession = mock(Session.class);
        User mockUser = mock(User.class);
        when(sessionRepository.findById(any())).thenReturn(Optional.of(mockSession));
        when(mockSession.getCoach()).thenReturn(mockUser);
        when(mockSession.getCoach().getId()).thenReturn(COACH_ID);
        when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        //WHEN
        SessionDto actualSessionDto = sessionService.addFeedbackForCoachee(1, COACH_ID, addCoacheeFeedbackDto);
        //THEN
        verify(sessionRepository).findById(any());
        verify(feedbackMapper).toEntity(any(AddFeedbackForCoacheeDto.class));
        verify(sessionMapper).toDto(any());
        assertThat(actualSessionDto).isEqualTo(sessionDto);
    }

    @Test
    void addFeedbackForCoachee_givenNonExistentSessionId_thenThrowIllegalArgumentException() {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sessionService.addFeedbackForCoachee(1, 2, addCoacheeFeedbackDto));
    }

    @Test
    void addFeedbackForCoachee_givenNonExistentUserId_thenThrowUnauthorizedException() {
        //GIVEN
        Session mockSession = mock(Session.class);
        User mockUser = mock(User.class);
        when(sessionRepository.findById(any())).thenReturn(Optional.of(mockSession));
        when(mockSession.getCoach()).thenReturn(mockUser);
        when(mockSession.getCoach().getId()).thenReturn(1);
        //WHEN
        //THEN
        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> sessionService.addFeedbackForCoachee(1, 2, addCoacheeFeedbackDto));
    }

    @Test
    void addFeedbackForCoach_givenSessionIdUserIdAndFeedbackDto_thenReturnSessionDto() {
        //GIVEN
        Session mockSession = mock(Session.class);
        User mockUser = mock(User.class);
        when(sessionRepository.findById(any())).thenReturn(Optional.of(mockSession));
        when(mockSession.getCoachee()).thenReturn(mockUser);
        when(mockSession.getCoachee().getId()).thenReturn(COACHEE_ID);
        when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        //WHEN
        SessionDto actualSessionDto = sessionService.addFeedbackForCoach(1, COACHEE_ID, addCoachFeedbackDto);
        //THEN
        verify(sessionRepository).findById(any());
        verify(feedbackMapper).toEntity(any(AddFeedbackForCoachDto.class));
        verify(sessionMapper).toDto(any());
        assertThat(actualSessionDto).isEqualTo(sessionDto);
    }

    @Test
    void addFeedbackForCoach_givenNonExistentSessionId_thenThrowIllegalArgumentException() {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sessionService.addFeedbackForCoach(1, 2, addCoachFeedbackDto));
    }

    @Test
    void addFeedbackForCoach_givenNonExistentUserId_thenThrowUnauthorizedException() {
        //GIVEN
        Session mockSession = mock(Session.class);
        User mockUser = mock(User.class);
        when(sessionRepository.findById(any())).thenReturn(Optional.of(mockSession));
        when(mockSession.getCoachee()).thenReturn(mockUser);
        when(mockSession.getCoachee().getId()).thenReturn(1);
        //WHEN
        //THEN
        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> sessionService.addFeedbackForCoach(1, 2, addCoachFeedbackDto));
    }


    @Test
    void addFeedbackForCoach_givenCoacheeHasNotGivenFeedback_thenNoXpAdded() throws Exception {
        //GIVEN
        when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        when(feedbackMapper.toEntity((AddFeedbackForCoachDto) any())).thenReturn(new FeedbackForCoach().setExplanation(addCoachFeedbackDto.getExplanation()).setUsefulness(addCoachFeedbackDto.getUsefulness()));
        Field CoachField = User.class.getDeclaredField("id");
        CoachField.setAccessible(true);
        CoachField.set(coach, COACH_ID);
        Field CoacheeField = User.class.getDeclaredField("id");
        CoacheeField.setAccessible(true);
        CoacheeField.set(coachee, COACHEE_ID);
        Field sessionField = Session.class.getDeclaredField("status");
        sessionField.setAccessible(true);
        sessionField.set(session, SessionStatus.WAITING_FEEDBACK);

        coach.setXp(0);

        //WHEN
        sessionService.addFeedbackForCoach(1, COACHEE_ID, addCoachFeedbackDto);

        //THEN
        assertThat(coach.getXp()).isEqualTo(0);
        assertThat(session.getFeedbackForCoachee()).isNull();

    }

    @Test
    void addFeedbackForCoach_givenCoacheeHasAlreadyGivenFeedback_thenXpAdded() throws Exception {
        //GIVEN
        when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        when(feedbackMapper.toEntity((AddFeedbackForCoachDto) any())).thenReturn(new FeedbackForCoach().setExplanation(addCoachFeedbackDto.getExplanation()).setUsefulness(addCoachFeedbackDto.getUsefulness()));
        when(feedbackMapper.toEntity((AddFeedbackForCoacheeDto) any())).thenReturn(new FeedbackForCoachee().setPreparedness(addCoacheeFeedbackDto.getPreparedness()).setWillingness(addCoacheeFeedbackDto.getWillingness()));
        when(userService.updateXp(coach,3)).thenCallRealMethod();
        Field CoachField = User.class.getDeclaredField("id");
        CoachField.setAccessible(true);
        CoachField.set(coach, COACH_ID);
        Field CoacheeField = User.class.getDeclaredField("id");
        CoacheeField.setAccessible(true);
        CoacheeField.set(coachee, COACHEE_ID);
        Field sessionField = Session.class.getDeclaredField("status");
        sessionField.setAccessible(true);
        sessionField.set(session, SessionStatus.WAITING_FEEDBACK);

        coach.setXp(0);
        sessionService.addFeedbackForCoach(1, COACHEE_ID, addCoachFeedbackDto);
        //WHEN
        sessionService.addFeedbackForCoachee(1, COACH_ID, addCoacheeFeedbackDto);
        //THEN
        assertThat(coach.getXp()).isEqualTo(3);
    }

    @Test
    void addFeedbackForCoachee_givenCoachHasNotGivenFeedback_thenNoXpAdded() throws Exception{
        //GIVEN
        when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        when(feedbackMapper.toEntity((AddFeedbackForCoacheeDto) any())).thenReturn(new FeedbackForCoachee().setPreparedness(addCoacheeFeedbackDto.getPreparedness()).setWillingness(addCoacheeFeedbackDto.getWillingness()));
        Field CoachField = User.class.getDeclaredField("id");
        CoachField.setAccessible(true);
        CoachField.set(coach, COACH_ID);
        Field CoacheeField = User.class.getDeclaredField("id");
        CoacheeField.setAccessible(true);
        CoacheeField.set(coachee, COACHEE_ID);
        Field sessionField = Session.class.getDeclaredField("status");
        sessionField.setAccessible(true);
        sessionField.set(session, SessionStatus.WAITING_FEEDBACK);

        coach.setXp(0);

        //WHEN
        sessionService.addFeedbackForCoachee(1, COACH_ID, addCoacheeFeedbackDto);

        //THEN
        assertThat(coach.getXp()).isEqualTo(0);
        assertThat(session.getFeedbackForCoach()).isNull();
    }

    @Test
    void addFeedbackForCoachee_givenCoachHasAlreadyGivenFeedback_thenXpAdded() throws Exception{
        //GIVEN
        when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(any())).thenReturn(sessionDto);
        when(feedbackMapper.toEntity((AddFeedbackForCoachDto) any())).thenReturn(new FeedbackForCoach().setExplanation(addCoachFeedbackDto.getExplanation()).setUsefulness(addCoachFeedbackDto.getUsefulness()));
        when(feedbackMapper.toEntity((AddFeedbackForCoacheeDto) any())).thenReturn(new FeedbackForCoachee().setPreparedness(addCoacheeFeedbackDto.getPreparedness()).setWillingness(addCoacheeFeedbackDto.getWillingness()));
        when(userService.updateXp(coach,3)).thenCallRealMethod();
        Field CoachField = User.class.getDeclaredField("id");
        CoachField.setAccessible(true);
        CoachField.set(coach, COACH_ID);
        Field CoacheeField = User.class.getDeclaredField("id");
        CoacheeField.setAccessible(true);
        CoacheeField.set(coachee, COACHEE_ID);
        Field sessionField = Session.class.getDeclaredField("status");
        sessionField.setAccessible(true);
        sessionField.set(session, SessionStatus.WAITING_FEEDBACK);

        coach.setXp(0);
        sessionService.addFeedbackForCoachee(1, COACH_ID, addCoacheeFeedbackDto);

        //WHEN
        sessionService.addFeedbackForCoach(1, COACHEE_ID, addCoachFeedbackDto);
        //THEN
        assertThat(coach.getXp()).isEqualTo(3);
    }
}
