package com.switchfully.spectangular.services;

import com.switchfully.spectangular.JSONObjectParser;
import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import com.switchfully.spectangular.exceptions.UnauthorizedException;
import com.switchfully.spectangular.mappers.SessionMapper;
import com.switchfully.spectangular.repository.SessionRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Base64;

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

    private void validateCreateSession(CreateSessionDto createSessionDto, String token){
        JSONObject tokenObject = JSONObjectParser.JwtTokenToJSONObject(token);

        if (!tokenObject.get("sub").equals(""+createSessionDto.getCoacheeId())){
            throw new UnauthorizedException("User can't make session for other users");
        }

        if (!userService.findUserById(createSessionDto.getCoachId()).getRole().equals(Role.COACH)){
            throw new IllegalArgumentException("Coach must have role coach");
        }
    }


}
