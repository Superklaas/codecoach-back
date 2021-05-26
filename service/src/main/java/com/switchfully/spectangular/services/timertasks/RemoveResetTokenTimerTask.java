package com.switchfully.spectangular.services.timertasks;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.repository.UserRepository;

import java.util.TimerTask;

public class RemoveResetTokenTimerTask extends TimerTask {
    private final User user;
    private final UserRepository userRepository;

    public RemoveResetTokenTimerTask(User user, UserRepository userRepository) {
        this.user = user;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        this.user.setResetToken(null);
        userRepository.save(user);
    }
}
