package com.switchfully.spectangular.dtos;

public class CreateSessionDto {

    private String subject;
    private String date;
    private String startTime;
    private String location;
    private int coachId;
    private int coacheeId;
    private String remarks;

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getLocation() {
        return location;
    }

    public int getCoachId() {
        return coachId;
    }

    public int getCoacheeId() {
        return coacheeId;
    }

    public String getRemarks() {
        return remarks;
    }

    public CreateSessionDto setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public CreateSessionDto setDate(String date) {
        this.date = date;
        return this;
    }

    public CreateSessionDto setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public CreateSessionDto setLocation(String location) {
        this.location = location;
        return this;
    }

    public CreateSessionDto setCoachId(int coachId) {
        this.coachId = coachId;
        return this;
    }

    public CreateSessionDto setCoacheeId(int coacheeId) {
        this.coacheeId = coacheeId;
        return this;
    }

    public CreateSessionDto setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }
}
