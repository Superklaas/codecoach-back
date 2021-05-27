package com.switchfully.spectangular.dtos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddFeedbackForCoacheeDto that = (AddFeedbackForCoacheeDto) o;
        return Objects.equals(preparedness, that.preparedness) && Objects.equals(willingness, that.willingness) && Objects.equals(positive, that.positive) && Objects.equals(negative, that.negative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preparedness, willingness, positive, negative);
    }
}
