package com.switchfully.spectangular.dtos;

import java.util.Objects;

public class FeedbackForCoacheeDto {
    private Short preparedness;
    private Short willingness;
    private String positive;
    private String negative;

    public Short getPreparedness() {
        return preparedness;
    }

    public FeedbackForCoacheeDto setPreparedness(Short preparedness) {
        this.preparedness = preparedness;
        return this;
    }

    public Short getWillingness() {
        return willingness;
    }

    public FeedbackForCoacheeDto setWillingness(Short willingness) {
        this.willingness = willingness;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public FeedbackForCoacheeDto setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegative() {
        return negative;
    }

    public FeedbackForCoacheeDto setNegative(String negative) {
        this.negative = negative;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackForCoacheeDto that = (FeedbackForCoacheeDto) o;
        return Objects.equals(preparedness, that.preparedness) && Objects.equals(willingness, that.willingness) && Objects.equals(positive, that.positive) && Objects.equals(negative, that.negative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preparedness, willingness, positive, negative);
    }
}
