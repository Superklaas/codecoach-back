package com.switchfully.spectangular.dtos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackForCoachDto that = (FeedbackForCoachDto) o;
        return Objects.equals(explanation, that.explanation) && Objects.equals(usefulness, that.usefulness) && Objects.equals(positive, that.positive) && Objects.equals(negative, that.negative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(explanation, usefulness, positive, negative);
    }
}
