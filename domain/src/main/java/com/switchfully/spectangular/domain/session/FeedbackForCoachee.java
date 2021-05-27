package com.switchfully.spectangular.domain.session;


import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FeedbackForCoachee {

    private Short preparedness;
    private Short willingness;
    private String positive;
    private String negative;

    public FeedbackForCoachee() {
    }

    public Short getPreparedness() {
        return preparedness;
    }

    public FeedbackForCoachee setPreparedness(Short preparedness) {
        assertValidScore(preparedness);
        this.preparedness = preparedness;
        return this;
    }

    public Short getWillingness() {
        return willingness;
    }

    public FeedbackForCoachee setWillingness(Short willingness) {
        assertValidScore(willingness);
        this.willingness = willingness;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public FeedbackForCoachee setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegative() {
        return negative;
    }

    public FeedbackForCoachee setNegative(String negative) {
        this.negative = negative;
        return this;
    }

    private void assertValidScore(Short score) {
        if (score == null || score < 1 || score > 5) {
            throw new IllegalArgumentException("Given score must be between 1 and 5");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackForCoachee that = (FeedbackForCoachee) o;
        return Objects.equals(preparedness, that.preparedness) && Objects.equals(willingness, that.willingness) && Objects.equals(positive, that.positive) && Objects.equals(negative, that.negative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preparedness, willingness, positive, negative);
    }
}
