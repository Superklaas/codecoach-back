package com.switchfully.spectangular.mappers;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CreateSessionDto;
import com.switchfully.spectangular.dtos.SessionDto;
import com.switchfully.spectangular.dtos.UpdateSessionDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionMapper {

    private final FeedbackMapper feedbackMapper;

    public SessionMapper(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

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
                .setCoacheeId(session.getCoachee().getId())
                .setCoachProfileName(session.getCoach().getProfileName())
                .setCoacheeProfileName(session.getCoachee().getProfileName())
                .setStatus(session.getStatus().name())
                .setFeedbackForCoach(feedbackMapper.toDto(session.getFeedbackForCoach()))
                .setFeedbackForCoachee(feedbackMapper.toDto(session.getFeedbackForCoachee()));
    }

    public List<SessionDto> toListOfDtos(List<Session> sessions){
        return sessions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Session applyToEntity(Session originalSession,UpdateSessionDto updateSessionDto, User coach, User coachee) {
        return originalSession
                .setCoach(coach)
                .setCoachee(coachee)
                .setSubject(updateSessionDto.getSubject())
                .setDate(LocalDate.parse(updateSessionDto.getDate()))
                .setStartTime(LocalTime.parse(updateSessionDto.getStartTime()))
                .setLocation(updateSessionDto.getLocation());
    }
}
