package com.switchfully.spectangular.mappers;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class SessionMapper {

    public Session toEntity(CreateSessionDto dto, User coach, User coachee){
        return new Session(
                dto.getSubject(),
                LocalDate.parse(dto.getDate()),
                LocalTime.parse(dto.getStartTime()),
                dto.getRemarks(),
                dto.getLocation(),
                coach,
                coachee
        );
    }

    public SessionDto toDto(Session session) {
        SessionDto sessionDto = new SessionDto();

        sessionDto.setId(session.getId());
        sessionDto.setSubject(session.getSubject());
        sessionDto.setDate(session.getDate().toString());
        sessionDto.setStartTime(session.getStartTime().toString());
        sessionDto.setLocation(session.getLocation());
        sessionDto.setRemarks(session.getRemarks());
        sessionDto.setCoach_id(session.getCoach().getId());
        sessionDto.setCoachee_id(session.getCoachee().getId());


        return sessionDto;
    }
}
