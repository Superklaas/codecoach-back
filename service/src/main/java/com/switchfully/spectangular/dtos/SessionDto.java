package com.switchfully.spectangular.dtos;

import java.util.Objects;

public class SessionDto {

    private int id;
    private String subject;
    private String date;
    private String startTime;
    private String location;
    private String remarks;
    private int coach_id;
    private int coachee_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    public int getCoachee_id() {
        return coachee_id;
    }

    public void setCoachee_id(int coachee_id) {
        this.coachee_id = coachee_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionDto that = (SessionDto) o;
        return coach_id == that.coach_id && coachee_id == that.coachee_id && Objects.equals(subject, that.subject) && Objects.equals(date, that.date) && Objects.equals(startTime, that.startTime) && Objects.equals(location, that.location) && Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, date, startTime, location, remarks, coach_id, coachee_id);
    }
}
