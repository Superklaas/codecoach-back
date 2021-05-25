package com.switchfully.spectangular.dtos;

public class FeedbackForCoachDto {

    Short explanation;
    Short usefulness;
    String positive;
    String negative;

    public Short getExplanation() {
        return explanation;
    }

    public FeedbackForCoachDto setExplanation(Short explanation) {
        this.explanation = explanation;
        return this;
    }

    public Short getUsefulness() {
        return usefulness;
    }

    public FeedbackForCoachDto setUsefulness(Short usefulness) {
        this.usefulness = usefulness;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public FeedbackForCoachDto setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegative() {
        return negative;
    }

    public FeedbackForCoachDto setNegative(String negative) {
        this.negative = negative;
        return this;
    }
}
