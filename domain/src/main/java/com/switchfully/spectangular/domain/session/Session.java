package com.switchfully.spectangular.domain.session;

import com.switchfully.spectangular.domain.Role;
import com.switchfully.spectangular.domain.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "sessions")
public class Session {

    public static final int MAX_REMARK_LENGTH = 255;
    public static final int MAX_LOCATION_LENGTH = 255;
    public static final int MAX_SUBJECT_LENGTH = 255;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "explanation", column = @Column(name = "fb_coachee2coach_explanation")),
            @AttributeOverride( name = "usefulness", column = @Column(name = "fb_coachee2coach_usefulness")),
            @AttributeOverride( name = "positive", column = @Column(name = "fb_coachee2coach_positive")),
            @AttributeOverride( name = "negative", column = @Column(name = "fb_coachee2coach_negative"))
    })
    private FeedbackForCoach feedbackForCoach;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "preparedness", column = @Column(name = "fb_coach2coachee_preparedness")),
            @AttributeOverride( name = "willingness", column = @Column(name = "fb_coach2coachee_willingness")),
            @AttributeOverride( name = "positive", column = @Column(name = "fb_coach2coachee_positive")),
            @AttributeOverride( name = "negative", column = @Column(name = "fb_coach2coachee_negative"))
    })
    private FeedbackForCoachee feedbackForCoachee;

    public Session(String subject, LocalDate date, LocalTime startTime, String remarks, String location, User coach, User coachee) {
        this.setSubject(subject);
        this.setDateTime(date, startTime);
        this.setLocation(location);
        this.setRemarks(remarks);
        this.setCoach(coach);
        this.setCoachee(coachee);
        this.status = SessionStatus.REQUESTED;
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
        if (subject == null) {
            throw new IllegalArgumentException("Subject must be specified");
        }
        subject = subject.trim();
        if (subject.length() == 0 || subject.length() > MAX_SUBJECT_LENGTH) {
            throw new IllegalArgumentException("Subject must be betweeN 1 and 255 characters long");
        }
        this.subject = subject;
    }

    public void setDateTime(LocalDate date, LocalTime startTime) {
        validateDate(date, startTime);
        this.date = date;
        this.startTime = startTime;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setLocation(String location) {
        if (location == null || location.length() > MAX_LOCATION_LENGTH) {
            throw new IllegalArgumentException("Location is required and can only be up to 255 characters");
        }
        this.location = location;
    }

    public void setRemarks(String remarks) {
        if (remarks != null && remarks.length() > MAX_REMARK_LENGTH) {
            throw new IllegalArgumentException("Remarks can only be up to 255 characters");
        }
        this.remarks = remarks;
    }

    public void setStatus(SessionStatus status) {
        this.getStatus().assertStateChange(status);
        this.status = status;
    }

    public void setCoach(User coach) {
        if (coach == null ||!coach.getRole().equals(Role.COACH)) {
            throw new IllegalArgumentException("Must set an actual coach for a session");
        }
        this.coach = coach;
    }

    public void setCoachee(User coachee) {
        if (coachee == null) {
            throw new IllegalArgumentException("Must set an actual user for the coachee");
        }
        this.coachee = coachee;
    }

    public FeedbackForCoach getFeedbackForCoach() {
        return feedbackForCoach;
    }

    public Session setFeedbackForCoach(FeedbackForCoach feedbackForCoach) {
        assertOpenToFeedback();
        this.feedbackForCoach = feedbackForCoach;
        checkIfAllFeedbackReceived();
        return this;
    }

    public FeedbackForCoachee getFeedbackForCoachee() {
        return feedbackForCoachee;
    }

    public Session setFeedbackForCoachee(FeedbackForCoachee feedbackForCoachee) {
        assertOpenToFeedback();
        this.feedbackForCoachee = feedbackForCoachee;
        checkIfAllFeedbackReceived();
        return this;
    }

    private void checkIfAllFeedbackReceived() {
        if (feedbackForCoach != null && feedbackForCoach.getUsefulness() != null && feedbackForCoachee != null && feedbackForCoachee.getPreparedness() != null) {
            status = SessionStatus.FEEDBACK_RECEIVED;
        }
    }

    public void autoUpdateSession(){
        if (this.getDateTime().isAfter(LocalDateTime.now())){
            return;
        }
        if (this.status == SessionStatus.REQUESTED){
            setStatus(SessionStatus.DECLINED_AUTOMATICALLY);
        }
        if (this.status == SessionStatus.ACCEPTED){
            setStatus(SessionStatus.WAITING_FEEDBACK);
        }
    }

    private void validateDate(LocalDate date, LocalTime time){
        if (LocalDateTime.of(date,time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Session can't be in the past");
        }
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

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(this.getDate(),this.getStartTime());
    }

    private void assertOpenToFeedback() {
        if (!getStatus().equals(SessionStatus.WAITING_FEEDBACK)) {
            throw new IllegalStateException("Cannot give feedback when the session is not waiting for feedback");
        }
    }

}
