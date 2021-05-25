package com.switchfully.spectangular.dtos;

public class AddFeedbackForCoacheeDto {
    private Short preparedness;
    private Short willingness;
    private String positive;
    private String negative;

    public Short getPreparedness() {
        return preparedness;
    }

    public AddFeedbackForCoacheeDto setPreparedness(Short preparedness) {
        this.preparedness = preparedness;
        return this;
    }

    public Short getWillingness() {
        return willingness;
    }

    public AddFeedbackForCoacheeDto setWillingness(Short willingness) {
        this.willingness = willingness;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public AddFeedbackForCoacheeDto setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegative() {
        return negative;
    }

    public AddFeedbackForCoacheeDto setNegative(String negative) {
        this.negative = negative;
        return this;
    }
}
