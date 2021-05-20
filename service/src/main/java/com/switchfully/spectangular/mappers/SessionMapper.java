package com.switchfully.spectangular.mappers;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
        return new SessionDto()
                .setId(session.getId())
                .setSubject(session.getSubject())
                .setDate(session.getDate().toString())
                .setStartTime(session.getStartTime().toString())
                .setLocation(session.getLocation())
                .setRemarks(session.getRemarks())
                .setCoachId(session.getCoach().getId())
                .setCoacheeId(session.getCoachee().getId());
    }

    public List<SessionDto> toListOfDtos(List<Session> sessions){
        return sessions.stream().map(session -> this.toDto(session)).collect(Collectors.toList());
    }
}
