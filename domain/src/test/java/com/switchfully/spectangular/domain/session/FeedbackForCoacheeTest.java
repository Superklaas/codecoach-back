package com.switchfully.spectangular.domain.session;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FeedbackForCoacheeTest {

    @ParameterizedTest
    @ValueSource(shorts = {-23, -1, 0, 6, 100})
    void setPreparedness_givenInvalidScore_thenThrowIllegalArgumentException(short input) {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FeedbackForCoachee().setPreparedness(input));
    }

    @ParameterizedTest
    @ValueSource(shorts = {-23, -1, 0, 6, 100})
    void setWillingness_givenInvalidScore_thenThrowIllegalArgumentException(short input) {
        //GIVEN
        //WHEN
        //THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FeedbackForCoachee().setWillingness(input));
    }

}
