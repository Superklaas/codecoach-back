package com.switchfully.spectangular.dtos;

public class CoachRequestDto {
    private String motivation;
    private String topic1;
    private String topic2;

    public CoachRequestDto setMotivation(String motivation) {
        this.motivation = motivation;
        return this;
    }

    public CoachRequestDto setTopic1(String topic1) {
        this.topic1 = topic1;
        return this;
    }

    public CoachRequestDto setTopic2(String topic2) {
        this.topic2 = topic2;
        return this;
    }

    public String getMotivation() {
        return motivation;
    }

    public String getTopic1() {
        return topic1;
    }

    public String getTopic2() {
        return topic2;
    }
}
