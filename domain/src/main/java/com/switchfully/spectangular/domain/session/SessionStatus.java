package com.switchfully.spectangular.domain.session;

import com.switchfully.spectangular.domain.Role;

import java.util.Collections;
import java.util.List;

public enum SessionStatus {

    REQUESTED(false, List.of()),
    ACCEPTED(false, List.of(Role.COACH)),
    WAITING_FEEDBACK(false, List.of()),
    REQUEST_CANCELLED_BY_COACHEE(true, List.of(Role.COACHEE)),
    SESSION_CANCELLED_BY_COACHEE(true, List.of(Role.COACHEE)),
    REQUEST_DECLINED(true, List.of(Role.COACH)),
    SESSION_CANCELLED_BY_COACH(true, List.of(Role.COACH)),
    DECLINED_AUTOMATICALLY(true, List.of()),
    FEEDBACK_RECEIVED(true, List.of(Role.COACH));

    private final boolean isFinished;
    private final List<Role> authorizedRoles;

    SessionStatus(boolean isFinished, List<Role> authorizedRoles) {
        this.isFinished = isFinished;
        this.authorizedRoles = authorizedRoles;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<Role> getAuthorizedRoles() {
        return Collections.unmodifiableList(authorizedRoles);
    }

    public boolean isValidStateChange(SessionStatus status){
        switch (status){
            case DECLINED_AUTOMATICALLY, REQUEST_CANCELLED_BY_COACHEE, REQUEST_DECLINED, ACCEPTED:
                return this == SessionStatus.REQUESTED;
            case WAITING_FEEDBACK, SESSION_CANCELLED_BY_COACH, SESSION_CANCELLED_BY_COACHEE:
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
