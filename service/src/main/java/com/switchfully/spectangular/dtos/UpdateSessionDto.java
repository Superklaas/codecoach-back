package com.switchfully.spectangular.dtos;

public class UpdateSessionDto {

    private String coachProfileName;
    private String coacheeProfileName;
    private String subject;
    private String date;
    private String startTime;
    private String location;

    public String getCoachProfileName() {
        return coachProfileName;
    }

    public UpdateSessionDto setCoachProfileName(String coachProfileName) {
        this.coachProfileName = coachProfileName;
        return this;
    }

    public String getCoacheeProfileName() {
        return coacheeProfileName;
    }

    public UpdateSessionDto setCoacheeProfileName(String coacheeProfileName) {
        this.coacheeProfileName = coacheeProfileName;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public UpdateSessionDto setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getDate() {
        return date;
    }

    public UpdateSessionDto setDate(String date) {
        this.date = date;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public UpdateSessionDto setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public UpdateSessionDto setLocation(String location) {
        this.location = location;
        return this;
    }
}
