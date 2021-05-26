package com.switchfully.spectangular.domain.session;

import javax.persistence.Embeddable;

@Embeddable
public class FeedbackForCoach {

    private Short explanation;
    private Short usefulness;
    private String positive;
    private String negative;

    public FeedbackForCoach() {
    }

    public Short getExplanation() {
        return explanation;
    }

    public FeedbackForCoach setExplanation(Short explanation) {
        assertValidScore(explanation);

        this.explanation = explanation;
        return this;
    }


    public Short getUsefulness() {
        return usefulness;
    }

    public FeedbackForCoach setUsefulness(Short usefulness) {
        assertValidScore(usefulness);

        this.usefulness = usefulness;
        return this;
    }

    public String getPositive() {
        return positive;
    }

    public FeedbackForCoach setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegative() {
        return negative;
    }

    public FeedbackForCoach setNegative(String negative) {
        this.negative = negative;
        return this;
    }

    private void assertValidScore(Short score) {
        if (score == null || score < 1 || score > 5) {
            throw new IllegalArgumentException("Given score must be between 1 and 5");
        }
    }
}
