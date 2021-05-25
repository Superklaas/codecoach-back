package com.switchfully.spectangular.domain.session;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionStatusTest {

    @Test
    void given_REQUESTED_State_WhenChecking_ACCEPTED_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.ACCEPTED)).isTrue();
    }

    @Test
    void given_REQUESTED_State_WhenChecking_DECLINED_AUTOMATICALLY_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.DECLINED_AUTOMATICALLY)).isTrue();
    }

    @Test
    void given_REQUESTED_State_WhenChecking_REQUEST_DECLINED_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.REQUEST_DECLINED)).isTrue();
    }

    @Test
    void given_REQUESTED_State_WhenChecking_DECLINED_REQUEST_CANCELLED_BY_COACHEE_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.REQUEST_CANCELLED_BY_COACHEE)).isTrue();
    }

    @Test
    void given_REQUESTED_State_WhenChecking_SESSION_CANCELLED_BY_COACHEE_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.SESSION_CANCELLED_BY_COACHEE)).isFalse();
    }

    @Test
    void given_REQUESTED_State_WhenChecking_SESSION_CANCELLED_BY_COACH_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.SESSION_CANCELLED_BY_COACH)).isFalse();
    }

    @Test
    void given_REQUESTED_State_WhenChecking_WAITING_FEEDBACK_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.WAITING_FEEDBACK)).isFalse();
    }

    @Test
    void given_REQUESTED_State_WhenChecking_FEEDBACK_RECEIVED_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.REQUESTED.isValidStateChange(SessionStatus.FEEDBACK_RECEIVED)).isFalse();
    }

    @Test
    void given_ACCEPTED_State_WhenChecking_WAITING_FEEDBACK_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.ACCEPTED.isValidStateChange(SessionStatus.WAITING_FEEDBACK)).isTrue();
    }

    @Test
    void given_ACCEPTED_State_WhenChecking_SESSION_CANCELLED_BY_COACHEE_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.ACCEPTED.isValidStateChange(SessionStatus.SESSION_CANCELLED_BY_COACHEE)).isTrue();
    }

    @Test
    void given_ACCEPTED_State_WhenChecking_SESSION_CANCELLED_BY_COACH_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.ACCEPTED.isValidStateChange(SessionStatus.SESSION_CANCELLED_BY_COACH)).isTrue();
    }

    @Test
    void given_ACCEPTED_State_WhenChecking_FEEDBACK_RECEIVED_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.ACCEPTED.isValidStateChange(SessionStatus.FEEDBACK_RECEIVED)).isFalse();
    }

    @Test
    void given_ACCEPTED_State_WhenChecking_REQUEST_CANCELLED_BY_COACHEE_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.ACCEPTED.isValidStateChange(SessionStatus.REQUEST_CANCELLED_BY_COACHEE)).isFalse();
    }

    @Test
    void given_ACCEPTED_State_WhenChecking_REQUEST_DECLINED_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.ACCEPTED.isValidStateChange(SessionStatus.REQUEST_DECLINED)).isFalse();
    }

    @Test
    void given_WAITING_FEEDBACK_State_WhenChecking_FEEDBACK_RECEIVED_IsValidStateChange_thenReturnTrue(){
        assertThat(SessionStatus.WAITING_FEEDBACK.isValidStateChange(SessionStatus.FEEDBACK_RECEIVED)).isTrue();
    }

    @Test
    void given_WAITING_FEEDBACK_State_WhenChecking_SESSION_CANCELLED_BY_COACH_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.WAITING_FEEDBACK.isValidStateChange(SessionStatus.SESSION_CANCELLED_BY_COACH)).isFalse();
    }

    @Test
    void given_WAITING_FEEDBACK_State_WhenChecking_SESSION_CANCELLED_BY_COACHEE_IsValidStateChange_thenReturnFalse(){
        assertThat(SessionStatus.WAITING_FEEDBACK.isValidStateChange(SessionStatus.SESSION_CANCELLED_BY_COACHEE)).isFalse();
    }

    private SessionStatus getRandomSessionStatus(){
        SessionStatus[] statuses = SessionStatus.values();
        int length = statuses.length;
        int randIndex = new Random().nextInt(length);
        return statuses[randIndex];
    }

    private SessionStatus getRandomFinishedSessionStatus(){
        SessionStatus status = getRandomSessionStatus();
        while (!status.isFinished()){
            status = getRandomSessionStatus();
        }
        return status;
    }

    @Test
    void given_AnyFinishedSessionStatus_WhenAssignAnyNewStatus_ThenReturnFalse(){
        for (int i = 0; i < 25; i++) {
            assertThat(getRandomFinishedSessionStatus().isValidStateChange(getRandomSessionStatus())).isFalse();
        }
    }

    void GivenInvalidStateChange_WhenAssertStateChange_ThenThrowIllegalStateException(){
        assertThrows(IllegalStateException.class,
                () -> getRandomFinishedSessionStatus().assertStateChange(getRandomSessionStatus()));

    }

}
