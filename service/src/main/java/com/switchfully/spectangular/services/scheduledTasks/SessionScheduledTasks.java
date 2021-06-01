package com.switchfully.spectangular.services.scheduledTasks;

import com.switchfully.spectangular.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionScheduledTasks {

    private final SessionService sessionService;
    private final Logger logger = LoggerFactory.getLogger(SessionScheduledTasks.class);

    public SessionScheduledTasks(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedDelay = 1800000)
    public void autoUpdateSessions() {
        logger.info("Scheduled task is being performed... auto updating all session statuses");
        sessionService.autoUpdateAllSession();
    }
}
