package com.switchfully.spectangular.dtos;

import com.switchfully.spectangular.domain.session.SessionStatus;

public class SessionStatusDto {
    SessionStatus status;

    public SessionStatus getStatus() {
        return status;
    }

    public SessionStatusDto setStatus(SessionStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "{" + "status=" + status + '}';
    }
}
