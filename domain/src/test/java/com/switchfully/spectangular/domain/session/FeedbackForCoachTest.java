package com.switchfully.spectangular.domain.session;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FeedbackForCoachTest {

    @ParameterizedTest
    @ValueSource(shorts = {-23, -1, 0, 6, 100})
    void setExplanation_givenInvalidScore_thenThrowIllegalArgumentException(short input) {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FeedbackForCoach().setExplanation(input));
    }

    @ParameterizedTest
    @ValueSource(shorts = {-23, -1, 0, 6, 100})
    void setUsefulness_givenInvalidScore_thenThrowIllegalArgumentException(short input) {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FeedbackForCoach().setUsefulness(input));
    }

}
