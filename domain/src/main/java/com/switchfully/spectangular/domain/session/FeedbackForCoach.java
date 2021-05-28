package com.switchfully.spectangular.domain.session;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FeedbackForCoach {

    public static final int MAX_FEEDBACK_LENGTH = 2000;
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
        assertWrittenFeedback(positive);
        this.positive = positive;
        return this;
    }

    private void assertWrittenFeedback(String feedback) {
        if (feedback != null && feedback.length() > MAX_FEEDBACK_LENGTH) {
            throw new IllegalArgumentException("Feedback length is max 2000 characters");
        }
    }

    public String getNegative() {
        return negative;
    }

    public FeedbackForCoach setNegative(String negative) {
        assertWrittenFeedback(negative);
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
        FeedbackForCoach that = (FeedbackForCoach) o;
        return Objects.equals(explanation, that.explanation) && Objects.equals(usefulness, that.usefulness) && Objects.equals(positive, that.positive) && Objects.equals(negative, that.negative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(explanation, usefulness, positive, negative);
    }
}
