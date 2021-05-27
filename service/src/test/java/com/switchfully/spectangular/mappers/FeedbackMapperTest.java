package com.switchfully.spectangular.mappers;

import com.switchfully.spectangular.domain.Topic;
import com.switchfully.spectangular.domain.session.FeedbackForCoach;
import com.switchfully.spectangular.domain.session.FeedbackForCoachee;
import com.switchfully.spectangular.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackMapperTest {
    private final FeedbackMapper feedbackMapper = new FeedbackMapper();

    private AddFeedbackForCoacheeDto addCoacheeFeedbackDto;
    private AddFeedbackForCoachDto addCoachFeedbackDto;
    private FeedbackForCoacheeDto coacheeFeedbackDto;
    private FeedbackForCoachDto coachFeedbackDto;
    private FeedbackForCoachee coacheeFeedback;
    private FeedbackForCoach coachFeedback;

    @BeforeEach
    void setUp() {
        //coachee object + dto's
        addCoacheeFeedbackDto = new AddFeedbackForCoacheeDto()
                .setPreparedness((short) 1)
                .setWillingness((short) 2)
                .setPositive("good good")
                .setNegative("none");
        coacheeFeedbackDto = new FeedbackForCoacheeDto()
                .setPreparedness((short) 1)
                .setWillingness((short) 2)
                .setPositive("good good")
                .setNegative("none");
        coacheeFeedback = new FeedbackForCoachee()
                .setPreparedness((short) 1)
                .setWillingness((short) 2)
                .setPositive("good good")
                .setNegative("none");

        //coach object + dto's
        addCoachFeedbackDto = new AddFeedbackForCoachDto()
                .setExplanation((short) 1)
                .setUsefulness((short) 2)
                .setPositive("good good")
                .setNegative("none");
        coachFeedbackDto = new FeedbackForCoachDto()
                .setExplanation((short) 1)
                .setUsefulness((short) 2)
                .setPositive("good good")
                .setNegative("none");
        coachFeedback = new FeedbackForCoach()
                .setExplanation((short) 1)
                .setUsefulness((short) 2)
                .setPositive("good good")
                .setNegative("none");
    }

    @Test
    void toEntity_givenAddFeedbackForCoachDto_thenReturnFeedbackForCoach() {
        //GIVEN
        //WHEN
        FeedbackForCoach actualFeedback = feedbackMapper.toEntity(addCoachFeedbackDto);
        //THEN
        assertThat(actualFeedback).isEqualTo(coachFeedback);
    }

    @Test
    void toEntity_givenAddFeedbackForCoacheeDto_thenReturnFeedbackForCoachee() {
        //GIVEN
        //WHEN
        FeedbackForCoachee actualFeedback = feedbackMapper.toEntity(addCoacheeFeedbackDto);
        //THEN
        assertThat(actualFeedback).isEqualTo(coacheeFeedback);
    }

    @Test
    void toDto_givenFeedbackForCoach_thenReturnFeedbackForCoachDto() {
        //GIVEN
        //WHEN
        FeedbackForCoachDto actualFeedbackDto = feedbackMapper.toDto(coachFeedback);
        //THEN
        assertThat(actualFeedbackDto).isEqualTo(coachFeedbackDto);
    }

    @Test
    void toDto_givenFeedbackForCoachee_thenReturnFeedbackForCoacheeDto() {
        //GIVEN
        //WHEN
        FeedbackForCoacheeDto actualFeedbackDto = feedbackMapper.toDto(coacheeFeedback);
        //THEN
        assertThat(actualFeedbackDto).isEqualTo(coacheeFeedbackDto);
    }


}
