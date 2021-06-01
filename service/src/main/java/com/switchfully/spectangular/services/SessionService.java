package com.switchfully.spectangular.services;

import com.switchfully.spectangular.JSONObjectParser;
import com.switchfully.spectangular.domain.Feature;
import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.domain.session.SessionStatus;
import com.switchfully.spectangular.dtos.AddFeedbackForCoachDto;
import com.switchfully.spectangular.dtos.AddFeedbackForCoacheeDto;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import com.switchfully.spectangular.exceptions.UnauthorizedException;
import com.switchfully.spectangular.mappers.FeedbackMapper;
import com.switchfully.spectangular.mappers.SessionMapper;
import com.switchfully.spectangular.repository.SessionRepository;
import com.switchfully.spectangular.services.mailing.EmailService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final SessionMapper sessionMapper;
    private final FeedbackMapper feedbackMapper;

    public SessionService(SessionRepository sessionRepository, UserService userService, EmailService emailService, SessionMapper sessionMapper, FeedbackMapper feedbackMapper) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.sessionMapper = sessionMapper;
        this.feedbackMapper = feedbackMapper;
    }

    public SessionDto createSession(CreateSessionDto createSessionDto, int uid) {
        validateCreateSession(createSessionDto, uid);
        User coach = userService.findUserById(createSessionDto.getCoachId());
        User coachee = userService.findUserById(createSessionDto.getCoacheeId());

        Session session = sessionRepository.save(sessionMapper.toEntity(createSessionDto, coach, coachee));
        emailService.mailForSessionRequest(session);
        return sessionMapper.toDto(session);
    }

    public List<SessionDto> getAllSessionByCoach(int uid) {
        User user = userService.findUserById(uid);

        if (!Feature.GET_ALL_COACHING_SESSION.getRoleList().contains(user.getRole())) {
            throw new IllegalArgumentException("user is not a coach");
        }

        List<Session> coachSessions = sessionRepository.findAllByCoach(user);
        updateStatusSessionList(coachSessions);
        return sessionMapper.toListOfDtos(coachSessions);
    }

    public List<SessionDto> getAllSessionByCoachee(int uid) {

        List<Session> coacheeSessions = sessionRepository.findAllByCoachee(userService.findUserById(uid));
        updateStatusSessionList(coacheeSessions);
        return sessionMapper.toListOfDtos(coacheeSessions);
    }

    public SessionDto updateSessionStatus(int uid, int id, SessionStatus status) {
        if (!userHasAuthorityToChangeState(uid, id, status)){
            throw new UnauthorizedException("user cant change status of another user");
        }

        Session session = findSessionById(id);

        session.setStatus(status);
        mailCorrespondingSessionStatus(session, status);

        return sessionMapper.toDto(session);
    }

    private void mailCorrespondingSessionStatus(Session session, SessionStatus status) {
        switch (status) {
            case ACCEPTED -> emailService.mailCoacheeForAcceptedSession(session);
            case REQUEST_DECLINED -> emailService.mailCoacheeForDeclinedSession(session);
            case SESSION_CANCELLED_BY_COACHEE -> emailService.mailCoachForSessionCancelledByCoachee(session);
            case SESSION_CANCELLED_BY_COACH -> emailService.mailCoacheeForSessionCancelledByCoach(session);
            case REQUEST_CANCELLED_BY_COACHEE -> emailService.mailCoachForSessionRequestCancelled(session);
        }
    }

    public boolean userHasAuthorityToChangeState(int uid, int sessionId, SessionStatus status) {
        User user = userService.findUserById(uid);
        Session session = this.findSessionById(sessionId);
        if (user.equals(session.getCoach())){
            return hacCoachAuthorityToChange(status);
        }
        if (user.equals(session.getCoachee())){
            return hacCoacheeAuthorityToChange(status);
        }
        return false;
    }

    private void updateStatusSessionList(List<Session> sessionList) {
        sessionList.stream()
                .filter(Session::autoUpdateSession)
                .forEach(session -> {
                    if (session.getStatus().equals(SessionStatus.DECLINED_AUTOMATICALLY)) {
                        emailService.mailForAutomaticallyDeclinedSession(session);
                    } else if (session.getStatus().equals(SessionStatus.WAITING_FEEDBACK)) {
                        emailService.mailForAskingFeedback(session);
                    }
                });
    }

    private int getIdFromJwtToken(String token) {
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);
        return Integer.parseInt(tokenObject.get("sub").toString());
    }

    private void validateCreateSession(CreateSessionDto createSessionDto, int uid) {

        if (uid != createSessionDto.getCoacheeId()) {
            throw new UnauthorizedException("User can't make session for other users");
        }

        if (!userService.findUserById(createSessionDto.getCoachId()).getRole().equals(Role.COACH)) {
            throw new IllegalArgumentException("Coach must have role coach");
        }
    }

    private Session findSessionById(int id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("session not found with id: " + id));
    }

    private boolean hacCoacheeAuthorityToChange(SessionStatus status) {
        return status.getAuthorizedRoles().contains(Role.COACHEE);
    }

    private boolean hacCoachAuthorityToChange(SessionStatus status) {
        return status.getAuthorizedRoles().contains(Role.COACH);
    }

    public SessionDto addFeedbackForCoach(int sessionId, int userId, AddFeedbackForCoachDto addFeedbackDto) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + sessionId));

        if (!session.getCoachee().getId().equals(userId)) {
            throw new UnauthorizedException("You are not coachee for this session, so you cannot give such feedback.");
        }

        session.setFeedbackForCoach(feedbackMapper.toEntity(addFeedbackDto));
        emailService.mailCoachForReceivedFeedback(session);
        return sessionMapper.toDto(session);
    }

    public SessionDto addFeedbackForCoachee(int sessionId, int userId, AddFeedbackForCoacheeDto addFeedbackDto) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("session not found with id: " + sessionId));

        if (!session.getCoach().getId().equals(userId)) {
            throw new UnauthorizedException("You are not coach for this session, so you cannot give such feedback");
        }

        session.setFeedbackForCoachee(feedbackMapper.toEntity(addFeedbackDto));
        emailService.mailCoacheeForReceivedFeedback(session);
        return sessionMapper.toDto(session);
    }

    public  List<SessionDto> getAll(){
        List<Session> sessions = sessionRepository.findAll();
        updateStatusSessionList(sessions);
        return sessionMapper.toListOfDtos(sessions);
    }
}
