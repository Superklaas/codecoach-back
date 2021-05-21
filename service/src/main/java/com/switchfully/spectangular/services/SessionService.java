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
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final SessionMapper sessionMapper;

    public SessionService(SessionRepository sessionRepository, UserService userService, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.sessionMapper = sessionMapper;
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
        if (user.getRole() != Role.COACH) {
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

    //TODO refactor
    public SessionDto updateSessionStatus(int id, SessionStatus status) {
        Optional<Session> optionalSession = sessionRepository.findById(id);

        if (optionalSession.isEmpty()) {
            throw new IllegalArgumentException("session not found with id: " + id);
        }

        Session session = optionalSession.get();

        session.setStatus(status);

        return sessionMapper.toDto(session);
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
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isEmpty()) {
            throw new IllegalArgumentException("session not found with id: " + id);
        }
        return session.get();
    }

    public boolean userhasAuthorityToChangeState(String token, int sessionId, SessionStatus status) {
        User user = userService.findUserById(this.getIdFromJwtToken(token));
        Session session = this.findSessionById(sessionId);
        if (user==session.getCoach()){
            return hacCoachAuthorityToChange(status);
        }
        if (user==session.getCoachee()){
            return hacCoacheeAuthorityToChange(status);
        }
        return false;
    }

    private boolean hacCoacheeAuthorityToChange(SessionStatus status) {
        return status.getAuthorizedRoles().contains(Role.COACHEE);
    }

    private boolean hacCoachAuthorityToChange(SessionStatus status) {
        return status.getAuthorizedRoles().contains(Role.COACH);
    }


}
