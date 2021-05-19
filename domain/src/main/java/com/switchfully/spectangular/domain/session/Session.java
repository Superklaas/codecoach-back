package com.switchfully.spectangular.domain.session;

import com.switchfully.spectangular.domain.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "location")
    private String location;

    @Column(name = "remarks")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SessionStatus status;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;

    @ManyToOne
    @JoinColumn(name = "coachee_id")
    private User coachee;

    public Session(String subject, LocalDate date, LocalTime startTime, String remarks, String location, User coach, User coachee) {
        validateDate(date, startTime);
        this.subject = subject;
        this.date = date;
        this.startTime = startTime;
        this.location = location;
        this.remarks = remarks;
        this.coach = coach;
        this.coachee = coachee;
        this.status = SessionStatus.REQUESTED;
    }

    private void validateDate(LocalDate date, LocalTime time){
        if (LocalDateTime.of(date,time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Session can't be in the past");
        }
    }

    public Session() {}

    public Integer getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getLocation() {
        return location;
    }

    public String getRemarks() {
        return remarks;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public User getCoach() {
        return coach;
    }

    public User getCoachee() {
        return coachee;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

    public void setCoachee(User coachee) {
        this.coachee = coachee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(subject, session.subject) && Objects.equals(date, session.date) && Objects.equals(startTime, session.startTime) && Objects.equals(location, session.location) && Objects.equals(remarks, session.remarks) && Objects.equals(coach, session.coach) && Objects.equals(coachee, session.coachee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, date, startTime, location, remarks, coach, coachee);
    }
}
