package com.switchfully.spectangular.dtos;

public class AddFeedbackForCoachDto {
    Short explanation;
    Short usefulness;
    String positive;
    String negative;

    public Short getExplanation() {
        return explanation;
    }

    public AddFeedbackForCoachDto setExplanation(Short explanation) {
        this.explanation = explanation;
        return this;
    }

    public Short getUsefulness() {
        return usefulness;
    }

    public AddFeedbackForCoachDto setUsefulness(Short usefulness) {
        this.usefulness = usefulness;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public AddFeedbackForCoachDto setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegative() {
        return negative;
    }

    public AddFeedbackForCoachDto setNegative(String negative) {
        this.negative = negative;
        return this;
    }
}
