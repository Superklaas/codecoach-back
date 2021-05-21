package com.switchfully.spectangular.domain.session;

public enum SessionStatus {

    REQUESTED(false),
    ACCEPTED(false),
    WAITING_FEEDBACK(false),
    REQUEST_CANCELLED_BY_COACHEE(true),
    SESSION_CANCELLED_BY_COACHEE(true),
    REQUEST_DECLINED(true),
    SESSION_CANCELLED_BY_COACH(true),
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
            case REQUEST_CANCELLED_BY_COACHEE:
            case REQUEST_DECLINED:
            case ACCEPTED:
                return this == SessionStatus.REQUESTED;
            case WAITING_FEEDBACK:
            case SESSION_CANCELLED_BY_COACH:
            case SESSION_CANCELLED_BY_COACHEE:
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
