package com.switchfully.spectangular.domain.session;


import javax.persistence.Embeddable;

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
}
