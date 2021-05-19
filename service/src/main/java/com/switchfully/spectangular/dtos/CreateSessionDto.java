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

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public int getCoacheeId() {
        return coacheeId;
    }

    public void setCoacheeId(int coacheeId) {
        this.coacheeId = coacheeId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
