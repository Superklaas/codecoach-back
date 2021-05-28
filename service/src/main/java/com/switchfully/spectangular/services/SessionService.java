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
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final SessionMapper sessionMapper;
    private final FeedbackMapper feedbackMapper;

    public SessionService(SessionRepository sessionRepository, UserService userService, SessionMapper sessionMapper, FeedbackMapper feedbackMapper) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.sessionMapper = sessionMapper;
        this.feedbackMapper = feedbackMapper;
    }

    public SessionDto createSession(CreateSessionDto createSessionDto, String token) {
        validateCreateSession(createSessionDto, token);
        User coach = userService.findUserById(createSessionDto.getCoachId());
        User coachee = userService.findUserById(createSessionDto.getCoacheeId());

        Session session = sessionMapper.toEntity(createSessionDto, coach, coachee);

        return sessionMapper.toDto(sessionRepository.save(session));
    }

    public List<SessionDto> getAllSessionByCoach(String token) {
        int id = getIdFromJwtToken(token);
        User user = userService.findUserById(id);
        if (!Feature.GET_ALL_COACHING_SESSION.getRoleList().contains(user.getRole())) {
            throw new IllegalArgumentException("user is not a coach");
        }

        List<Session> coachSessions = sessionRepository.findAllByCoach(user);
        updateStatusSessionList(coachSessions);
        return sessionMapper.toListOfDtos(coachSessions);
    }

    public List<SessionDto> getAllSessionByCoachee(String token) {
        int id = getIdFromJwtToken(token);
        List<Session> coacheeSessions = sessionRepository.findAllByCoachee(userService.findUserById(id));
        updateStatusSessionList(coacheeSessions);
        return sessionMapper.toListOfDtos(coacheeSessions);
    }

    public SessionDto updateSessionStatus(int id, SessionStatus status) {
        Session session = findSessionById(id);

        session.setStatus(status);

        return sessionMapper.toDto(session);
    }

    public boolean userHasAuthorityToChangeState(String token, int sessionId, SessionStatus status) {
        User user = userService.findUserById(this.getIdFromJwtToken(token));
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
        sessionList.forEach(Session::autoUpdateSession);
    }

    private int getIdFromJwtToken(String token) {
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);
        return Integer.parseInt(tokenObject.get("sub").toString());
    }

    private void validateCreateSession(CreateSessionDto createSessionDto, String token) {
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);

        if (!tokenObject.get("sub").equals("" + createSessionDto.getCoacheeId())) {
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
                .orElseThrow(() -> new IllegalArgumentException("session not found with id: " + sessionId));

        if (!session.getCoachee().getId().equals(userId)) {
            throw new UnauthorizedException("You are not coachee for this session, so you cannot give such feedback");
        }

        session.setFeedbackForCoach(feedbackMapper.toEntity(addFeedbackDto));
        return sessionMapper.toDto(session);
    }


    public SessionDto addFeedbackForCoachee(int sessionId, int userId, AddFeedbackForCoacheeDto addFeedbackDto) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("session not found with id: " + sessionId));

        if (!session.getCoach().getId().equals(userId)) {
            throw new UnauthorizedException("You are not coach for this session, so you cannot give such feedback");
        }

        session.setFeedbackForCoachee(feedbackMapper.toEntity(addFeedbackDto));
        return sessionMapper.toDto(session);
    }
}
