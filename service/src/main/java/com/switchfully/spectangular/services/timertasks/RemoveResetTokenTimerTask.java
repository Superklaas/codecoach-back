package com.switchfully.spectangular.services.timertasks;

import com.switchfully.spectangular.domain.User;

import java.util.TimerTask;

public class RemoveResetTokenTimerTask extends TimerTask {
    private final User user;

    public RemoveResetTokenTimerTask(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        this.user.setResetToken(null);
    }
}
