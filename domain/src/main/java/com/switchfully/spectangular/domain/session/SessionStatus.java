package com.switchfully.spectangular.domain.session;

import org.hibernate.Session;

public enum SessionStatus {

    REQUESTED(false),
    ACCEPTED(false),
    WAITING_FEEDBACK(false),
    DECLINED_BY_COACHEE(true),
    CANCELLED_BY_COACHEE(true),
    DECLINED_BY_COACH(true),
    CANCELLED_BY_COACH(true),
    DECLINED_AUTOMATICALLY(true),
    FEEDBACK_RECEIVED(true);

    private final boolean isFinished;

    SessionStatus(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isValidStateChange(SessionStatus status){
        switch (status){
            case DECLINED_AUTOMATICALLY:
            case DECLINED_BY_COACHEE:
            case DECLINED_BY_COACH:
            case ACCEPTED:
                return this == SessionStatus.REQUESTED;
            case WAITING_FEEDBACK:
            case CANCELLED_BY_COACH:
            case CANCELLED_BY_COACHEE:
               return this == SessionStatus.ACCEPTED;
            case FEEDBACK_RECEIVED:
                return this == SessionStatus.WAITING_FEEDBACK;
            default:
                throw new IllegalStateException("Unexpected statechange happened: " + this.name() + " -> " + status.name());
        }
    }

    public void assertStateChange(SessionStatus status){
        if (!isValidStateChange(status)) {
            throw new IllegalStateException("Invalid sessionStatus change:" + this.name() + " -> " + status.name());
        }
    }


}
